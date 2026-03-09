package com.cht.directory.connector.filter.service;

import com.cht.directory.connector.filter.log.entity.AuditLogs;
import com.cht.directory.connector.filter.log.repository.AuditLogsRepository;
import com.cht.directory.connector.filter.log.entity.ConnectorSpaceAdPersonDetails;
import com.cht.directory.connector.filter.log.repository.ConnectorSpaceAdPersonDetailsRepository;
import com.cht.directory.connector.security.KmsClientService;
import com.cht.org.jose4j.jwk.JsonWebKey;
import com.cht.directory.connector.filter.h2.entity.Users;
import com.cht.directory.connector.filter.h2.repository.UsersRepository;
import com.cht.directory.connector.filter.security.DataUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PublishPwdService {

    private static final Logger log = LoggerFactory.getLogger(PublishPwdService.class);

    // 因為外網的 ad 資料會同步回內網，所以需要註明 region 以找尋 table 對應的 placeholder 欄位
    @Value("${server.logRegion:inner}")
    private String region;

    @Autowired
    ConnectorSpaceAdPersonDetailsRepository connectorSpaceAdPersonDetailsRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    AuditLogsRepository auditService;

    @Autowired
    private KmsClientService kmsClientService;

    public void publish(Map<String,String> message) {
        log.info("user_name: " + message.get("user_name"));
        //log.info("new_password: " + message.get("new_password"));
        String encryptedPwd = "";
        String hashPwd = "";
        //排除無法解釋的ad帳密行為
        if (message.get("user_name") != null && message.get("user_name").contains("$")) {
            log.error("user name: " + message.get("user_name") + " contains special char '$'!");
            return;
        } else if (message.get("user_name") == null || message.get("user_name").isEmpty()) {
            log.error("user name: " + message.get("user_name") + " is invalid!");
            return;
        } else if (message.get("new_password") == null || message.get("new_password").isEmpty()) {
            log.error("new password: " + message.get("new_password") + " is invalid!");
            return;
        }

        try {
            long startTime = System.currentTimeMillis();
            // 加密密碼
            byte pwdArray[] = DataUtils.toQuoteUnicode(message.get("new_password"));
            JsonWebKey key = DataUtils.loadKeyFromFile(new File("C:\\AdPwdSync\\adws.jceks"), "1qaz@WSX".toCharArray());
            encryptedPwd = DataUtils.encrypt(key, message.get("user_name"), pwdArray);
            // 將密碼 hash
            hashPwd = DataUtils.hash(pwdArray);

            if (!encryptedPwd.isEmpty()) {
                log.info("encrypted password: " + encryptedPwd);
                log.info("hash password: " + hashPwd);

                // 準備log
                AuditLogs auditLog = new AuditLogs();
                auditLog.setRegion(region);
                auditLog.setCategory("PasswordManagement");
                auditLog.setActivitydisplayname("User password filter");
                auditLog.setActivitydatetime(new Timestamp(new Date().getTime()));
                auditLog.setCorrelationid("password filter");
                auditLog.setInitiatedby("password filter");
                auditLog.setLoggedbyservice("ad-pwd-filter");
                auditLog.setResultcode(999);
                auditLog.setTargetresources(message.get("user_name"));
                auditLog.setResult("FAILURE");

                Optional<ConnectorSpaceAdPersonDetails> ConnectorSpaceAdPersonDetailsOptional =
                        connectorSpaceAdPersonDetailsRepository.findBySamaccountnameAndPlaceholder(message.get("user_name"), region);
                // 有找到符合的sAMAccountName才進行密碼變更
                if (ConnectorSpaceAdPersonDetailsOptional.isPresent()) {
                    // 更新密碼與最後的密碼變更時間
                    ConnectorSpaceAdPersonDetails personDetail = ConnectorSpaceAdPersonDetailsOptional.get();
                    personDetail.setUnicodepwd(encryptedPwd);
                    personDetail.setUnicodepwdHash(hashPwd);
                    personDetail.setPwdlastset(new Timestamp(new Date().getTime()));
                    connectorSpaceAdPersonDetailsRepository.save(personDetail);
                    log.info("update cn: " + message.get("user_name") + " password successful");

                    // 補上成功資訊
                    auditLog.setResultcode(0);
                    auditLog.setResult("SUCCESS");
                    auditLog.setAdditionaldetails("change cn:" + message.get("user_name") + ", hash password: " + hashPwd + ", encrypted password: " + encryptedPwd);

                    // 正常處理後保留一份資料到 h2
                    Optional<Users> usersOpt = usersRepository.findById(message.get("user_name"));
                    Users users = usersOpt.orElse(new Users(message.get("user_name"),
                            encryptedPwd, hashPwd, new Timestamp(new Date().getTime()), 0, true));
                    users.setActivetime(new Timestamp(new Date().getTime()));
                    users.setPassword(encryptedPwd);
                    users.setHashcode(hashPwd);
                    users.setEcounter(0);
                    users.setStatus(true);
                    usersRepository.save(users);
                } else {
                    log.info("can't find cn: " + message.get("user_name") + " info in db, try again later!");
                    auditLog.setAdditionaldetails("password filter trigger by " + message.get("user_name") + " but cn not found in db");

                    // 找不到 cn 應為新建帳號， ad 資料還未 pull 下來，先放進h2等後重試
                    Optional<Users> usersOpt = usersRepository.findById(message.get("user_name"));
                    Users users = usersOpt.orElse(new Users(message.get("user_name"),
                            encryptedPwd, hashPwd, new Timestamp(new Date().getTime()), 0, true));
                    users.setActivetime(new Timestamp(new Date().getTime()));
                    users.setPassword(encryptedPwd);
                    users.setHashcode(hashPwd);
                    // 找不到 cn 代表密碼變更失敗，ecounter +1，試30次就不再重試，因為可能是非MOF帳號
                    users.setEcounter(users.getEcounter() + 1);
                    users.setStatus(false);
                    usersRepository.save(users);
                }
                long endTime = System.currentTimeMillis();

                // 寫入 log
                auditLog.setDurationinmilliseconds(endTime - startTime);
                try {
                    auditService.save(auditLog);
                } catch (Exception e) {
                    log.error("write log to db fail!");
                }
            } else {
                log.info("change cn:" + message.get("user_name") + " but encrypted password is empty!");

                // 理論上應該不會有這種情況，先不做後續處理
            }
        } catch (Exception e) {
            e.printStackTrace();

            // IDM 資料庫寫入失敗的話也寫入h2後補(資料庫連不上的狀況下不累計失敗次數，會一直重試)
            Optional<Users> usersOpt = usersRepository.findById(message.get("user_name"));
            // 資料庫連線失敗的話不累加ecounter
            Users users = usersOpt.orElse(new Users(message.get("user_name"),
                    encryptedPwd, hashPwd, new Timestamp(new Date().getTime()), 0, true));
            users.setActivetime(new Timestamp(new Date().getTime()));
            users.setPassword(encryptedPwd);
            users.setHashcode(hashPwd);
            users.setStatus(false);
            usersRepository.save(users);
        }

    }

    public void retry() {
        // 定期從h2取出失敗的密碼變更重試(超過10次的就不再重試，直到api重新收到該帳號的變更->ecoutner歸0)
        List<Users> pendingUsers = usersRepository.findByStatusFalseAndEcounterLessThanEqual(29);
        if (!pendingUsers.isEmpty()) {
            log.info("retry request from h2 => size: " + pendingUsers.size());
        }

        for (Users user : pendingUsers) {
            try {
                String username = user.getUsername();
                String newPassword = "";
                byte[] unicodePwd = kmsClientService.decrypt(username, user.getPassword());
                newPassword = DataUtils.fromQuoteUnicode(unicodePwd);

                Map<String, String> message = Map.of(
                        "user_name", username,
                        "new_password", newPassword // 解密或原樣，依你邏輯
                );

                log.info("Retry publishing for user: " + user.getUsername());
                publish(message);
            } catch (Exception e) {
                log.error("Retry failed for user: " + user.getUsername(), e);
            }
        }
    }
}
