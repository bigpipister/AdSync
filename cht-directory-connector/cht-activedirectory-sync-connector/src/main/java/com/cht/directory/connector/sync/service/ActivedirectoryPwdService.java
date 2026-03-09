package com.cht.directory.connector.sync.service;

import com.cht.directory.connector.security.KmsClientService;
import com.cht.directory.connector.security.utils.DataUtils;
import com.cht.directory.connector.service.ActiveDirectoryService;
import com.cht.directory.connector.service.EventLogsService;
import com.cht.directory.connector.service.StatusLogsService;
import com.cht.directory.connector.sync.config.ServiceParameters;
import com.cht.directory.domain.ConnectorSpaceAdPersonDetails;
import com.cht.directory.domain.ConnectorSpaceAdPersonExternalDetails;
import com.cht.directory.repository.ConnectorSpaceAdPersonExternalDetailsRepository;
import com.unboundid.ldap.sdk.LDAPResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ActivedirectoryPwdService {

    @Autowired
    private ServiceParameters serviceParameters;

    @Autowired
    private ActiveDirectoryService activeDirectoryService;

    @Autowired
    private KmsClientService kmsClientService;

    @Autowired
    private ConnectorSpaceAdPersonExternalDetailsRepository connectorSpaceAdPersonExternalDetailsRepository;

    @Autowired
    private StatusLogsService statusLogsService;

    @Autowired
    private EventLogsService eventLogsService;

    private String health = "Y";

    public void savePassWordChange() throws Exception {

        log.info("ServiceParameters : {}", serviceParameters);

        Boolean running = true;
        health = "Y";
        Date date = new Date();
        Timestamp startDateTime = new Timestamp(date.getTime());
        long startTime = System.currentTimeMillis(); // 取得開始時間 (毫秒)
        statusLogsService.record("pwd", serviceParameters.getBasedn(), "同步AD密碼",
                startDateTime, running, health, 0);

        try {
            eventLogsService.del("pwd", serviceParameters.getBasedn());

            // 外網
            if (StringUtils.equalsIgnoreCase("external",
                    serviceParameters.getPlaceholder())) {

                performExternalPasswordChange();
            }

            // HE: exception log 測試
            //throw new Exception("ActivedirectoryPwdService excpeiton test");
        } catch (Exception ex) {

            String stackTrace = ExceptionUtils.getStackTrace(ex);
            // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
            stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
            log.error("{}", stackTrace);
            eventLogsService.record("pwd", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
            health = "N";
        }

        running = false;
        long endTime = System.currentTimeMillis(); // 取得結束時間 (毫秒)
        long duration = (endTime - startTime) / 1000; // 轉換為秒數
        statusLogsService.record("pwd", serviceParameters.getBasedn(), "同步AD密碼",
                startDateTime, running, health, duration);
    }

    /**
     * 外網密碼變更 (中心/五區)
     *
     * @throws Exception
     */
    private void performExternalPasswordChange() throws Exception {

        // 只對指定basedn下的檢查密碼不相等，來源端objectguid不得為-1且變更時間需大於目的端
        List<Object[]> chgPassList = connectorSpaceAdPersonExternalDetailsRepository
                .findAllPersonDetailsWhenPwdChanged(serviceParameters.getBasedn());

        // log.info("External password change list size: {}", chgPassList.size());
        int chgCount = 0;
        for (Object[] entrys : chgPassList) {

            ConnectorSpaceAdPersonExternalDetails connectorSpaceAdPersonExternalDetails = (ConnectorSpaceAdPersonExternalDetails) entrys[0];
            ConnectorSpaceAdPersonDetails connectorSpaceAdPersonDetails = (ConnectorSpaceAdPersonDetails) entrys[1];

            try {
                if (StringUtils.isNotBlank(connectorSpaceAdPersonExternalDetails.getUnicodepwd())) {

                    chgCount += 1;
                    log.info(
                            "(Dryrun : {}) user <{}> inner password ({}) is updated, change ad password ({}) to <{}>",
                            serviceParameters.isDryrun(),
                            connectorSpaceAdPersonExternalDetails.getDn(),
                            connectorSpaceAdPersonExternalDetails.getPwdlastset(),
                            connectorSpaceAdPersonDetails.getPwdlastset(),
                            connectorSpaceAdPersonExternalDetails.getUnicodepwd());

                    if (!serviceParameters.isDryrun()) {

//                        byte[] unicodePwd = kmsClientService.decrypt(connectorSpaceAdPersonSyncDetails.getCn(),
//                                connectorSpaceAdPersonSyncDetails.getUnicodepwd());
                        // pwd filter攔載到的其實是 samAccountName
                        byte[] unicodePwd = kmsClientService.decrypt(connectorSpaceAdPersonExternalDetails.getSamaccountname(),
                                connectorSpaceAdPersonExternalDetails.getUnicodepwd());

                        LDAPResult result = activeDirectoryService.changeUserPassword(
                                connectorSpaceAdPersonExternalDetails.getDn(), unicodePwd,
                                connectorSpaceAdPersonExternalDetails.getUnicodepwdHash(),
                                connectorSpaceAdPersonExternalDetails.getPwdlastset(),
                                connectorSpaceAdPersonDetails.getPwdlastset());

                        log.info("result = {}", result);

                        result = activeDirectoryService
                                .unlockOrganizationPerson(connectorSpaceAdPersonExternalDetails.getDn());

                        log.info("result = {}", result);
                    }
                }

                // 內外網都必須要有密碼變更時間才能進行後續處理
//                if (!ObjectUtils.isEmpty(connectorSpaceAdPersonExternalDetails.getPwdlastset())
//                        && !ObjectUtils.isEmpty(connectorSpaceAdPersonDetails.getPwdlastset())) {
//
//                    // 有可能在同步失效的時候使用者會直接改外網密碼，所以先不以內網密碼強制覆蓋
//                    if (connectorSpaceAdPersonExternalDetails.getPwdlastset()
//                            .after(connectorSpaceAdPersonDetails.getPwdlastset())) {
//                        chgCount += 1;
//                        log.info(
//                                "(Dryrun : {}) user <{}> inner password ({}) is updated, change ad password ({}) to <{}>",
//                                serviceParameters.isDryrun(),
//                                connectorSpaceAdPersonExternalDetails.getDn(),
//                                connectorSpaceAdPersonExternalDetails.getPwdlastset(),
//                                connectorSpaceAdPersonDetails.getPwdlastset(),
//                                connectorSpaceAdPersonExternalDetails.getUnicodepwd());
//
//                        if (!serviceParameters.isDryrun()) {
//
////                        byte[] unicodePwd = kmsClientService.decrypt(connectorSpaceAdPersonSyncDetails.getCn(),
////                                connectorSpaceAdPersonSyncDetails.getUnicodepwd());
//                            // pwd filter攔載到的其實是 samAccountName
//                            byte[] unicodePwd = kmsClientService.decrypt(connectorSpaceAdPersonExternalDetails.getSamaccountname(),
//                                    connectorSpaceAdPersonExternalDetails.getUnicodepwd());
//
//                            LDAPResult result = activeDirectoryService.changeUserPassword(
//                                    connectorSpaceAdPersonExternalDetails.getDn(), unicodePwd,
//                                    connectorSpaceAdPersonExternalDetails.getUnicodepwdHash(),
//                                    connectorSpaceAdPersonExternalDetails.getPwdlastset(),
//                                    connectorSpaceAdPersonDetails.getPwdlastset());
//
//                            log.info("result = {}", result);
//
//                            result = activeDirectoryService
//                                    .unlockOrganizationPerson(connectorSpaceAdPersonExternalDetails.getDn());
//
//                            log.info("result = {}", result);
//                        }
//                    }
//                } else {
//
//                    log.warn(
//                            "(Dryrun : {}) user <{}> edirectory password is updated, but password is blank <{}>",
//                            serviceParameters.isDryrun(), connectorSpaceAdPersonExternalDetails.getDn(),
//                            connectorSpaceAdPersonDetails.getUnicodepwd());
//                }
            } catch (Exception ex) {

                String stackTrace = ExceptionUtils.getStackTrace(ex);
                stackTrace = "dn: " + connectorSpaceAdPersonExternalDetails.getDn() + "\n" + stackTrace;
                // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                log.error("{}", stackTrace);
                eventLogsService.record("pwd", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                health = "N";
            }
        }
        log.info("External password change list size: {}", chgCount);
    }

    // sample
//    public void changeEdirUserPassword() throws Exception {
//
//        // 解密
//        byte[] unicodePwd = kmsClientService.decrypt("NA00299", "");
//
//        // 轉換 unicode password to utf-8 password
//        // DataUtils.fromQuoteUnicode(unicodePwd);
//
//        edirectoryService.changeUserPassword("CN=NA00299,OU=A030600,OU=A03,OU=A05ROOT,O=MOF",
//                "1qaz@WSX", "", null, null);
//    }

    // sample
    public void changeAdUserPassword() throws Exception {

        // 解密
        // byte[] unicodePwd = kmsClientService.decrypt("NA00299", "");

        // DB unicodePwd 欄位內解密後已經是 QuoteUnicode 形式，故不需做轉換的動作。
        // 以下僅為測試用
        String password = "!QAZ2wsx3edc";
        byte[] unicodePwd = DataUtils.toQuoteUnicode(password);

        activeDirectoryService.changeUserPassword(
                "CN=testtest,OU=B011A00,OU=B01,OU=B01ROOT,OU=MOF,DC=intbca,DC=gov,DC=tw", unicodePwd,
                "", null, null);
    }

}
