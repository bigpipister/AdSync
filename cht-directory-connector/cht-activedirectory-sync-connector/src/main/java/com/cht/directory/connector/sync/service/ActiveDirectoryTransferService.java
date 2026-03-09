package com.cht.directory.connector.sync.service;

import com.cht.directory.connector.security.KmsClientService;
import com.cht.directory.connector.security.utils.DataUtils;
import com.cht.directory.connector.service.ActiveDirectoryService;
import com.cht.directory.connector.service.EventLogsService;
import com.cht.directory.connector.service.StatusLogsService;
import com.cht.directory.connector.service.utils.DateTimeUtils;
import com.cht.directory.connector.sync.config.ServiceParameters;
import com.cht.directory.domain.ConnectorSpaceAdPersonFiaDetails;
import com.cht.directory.repository.ConnectorSpaceAdPersonFiaDetailsRepository;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

@Component
@Slf4j
public class ActiveDirectoryTransferService {

    private static final String[] ORGANIZATIONPERSON_ATTRIBUTES = new String[] { "cn", "pwdLastSet" };

    @Autowired
    private ServiceParameters serviceParameters;

    @Autowired
    private KmsClientService kmsClientService;

    @Autowired
    private ActiveDirectoryService activeDirectoryService;

    @Value("${activedirectory.service.upndomain}")
    private String upndomain;

    @Autowired
    private ConnectorSpaceAdPersonFiaDetailsRepository connectorSpaceAdPersonFiaDetailsRepository;

    @Autowired
    private StatusLogsService statusLogsService;

    @Autowired
    private EventLogsService eventLogsService;

    private String health = "Y";

    public void executeTransfer() throws Exception {

        log.info("ServiceParameters : {}", serviceParameters);

        Boolean running = true;
        health = "Y";
        Date date = new Date();
        Timestamp startDateTime = new Timestamp(date.getTime());
        long startTime = System.currentTimeMillis(); // 取得開始時間 (毫秒)
        statusLogsService.record("trans", serviceParameters.getBasedn(), "匯整五區帳號",
                startDateTime, running, health, 0);

        try {
            eventLogsService.del("trans", serviceParameters.getBasedn());
            // 針對存在五區 ad 但不在中心匯整 ou 的 person -> 新增
            // 針對不存在五區 ad 但存在中心匯整 ou 的 person -> 刪除
            syncOrganizationPerson();
        } catch (Exception ex) {

            String stackTrace = ExceptionUtils.getStackTrace(ex);
            // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
            stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
            log.error("{}", stackTrace);
            eventLogsService.record("trans", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
            health = "N";
        }

        running = false;
        long endTime = System.currentTimeMillis(); // 取得結束時間 (毫秒)
        long duration = (endTime - startTime) / 1000; // 轉換為秒數
        statusLogsService.record("trans", serviceParameters.getBasedn(), "匯整五區帳號",
                startDateTime, running, health, duration);
    }

    public void syncOrganizationPerson() throws Exception {
        log.info("Begin find all AHBDE OU person from ActiveDirectory OrganizationPerson...");

        SearchResult searchResult = activeDirectoryService.searchOrganizationPerson(
                serviceParameters.getTargetdn(), null, ORGANIZATIONPERSON_ATTRIBUTES);

        log.info("{}", searchResult);

        // 存放cn與密碼最後變更時間
        Map<String, Timestamp> cnMap = new HashMap<>();
        // 目標ad的cn列表
        List<String> cnList = new ArrayList<>();

        // 要加這個，否則一筆ad cn都沒有時db會抓不出資料
        cnList.add("");
        for (SearchResultEntry entry : searchResult.getSearchEntries()) {

            Attribute cnAttr = entry.getAttribute("cn");
            Attribute pwdLastSetAttr = entry.getAttribute("pwdLastSet");

            if (null != cnAttr) {
                cnList.add(cnAttr.getValue());
                if (StringUtils.isNotEmpty(pwdLastSetAttr.getValue())
                        && !StringUtils.equals(pwdLastSetAttr.getValue(), "0")) {
                    cnMap.put(cnAttr.getValue(), DateTimeUtils.msADtoTimestamp(pwdLastSetAttr.getValue()));
                }
            }
        }
        //log.info(cnMap.toString());

        // 需新增(在資料庫找不在cn清單且沒被標註刪除、密碼不為空的資料)
        List<ConnectorSpaceAdPersonFiaDetails> connectorSpaceAdPersonFiaDetailsListAdd = connectorSpaceAdPersonFiaDetailsRepository
                .findAllByCnNotInAndPlaceholderAndObjectguidNotAndUnicodepwdIsNotNull(cnList, serviceParameters.getPlaceholder(),"-1");

        log.info("ActiveDirectory OrganizationPerson will be added list size: {}", connectorSpaceAdPersonFiaDetailsListAdd.size());

//        String passwordTest = "!QAZ2wsx3edc";
//        byte[] unicodePwdTest = DataUtils.toQuoteUnicode(passwordTest);
//        Date date = new Date();
//        Timestamp timestamp = new Timestamp(date.getTime());
//
//        LDAPResult addResult = activeDirectoryService.addOrganizationPerson(
//                "CN=user2,OU=AHBDE,DC=fia,DC=gov,DC=tw",
//                "user2", "NB00002", "user2", "AHBDE",
//                "1", "0", timestamp,
//                "NB00002@fia.gov.tw", "NB00002",
//                "user2", "B011A00首長室", null,
//                null, null, null,
//                null, null, "user2(NB00002)",
//                unicodePwdTest, 512, "ACTIVE", "1");

        for (ConnectorSpaceAdPersonFiaDetails connectorSpaceAdPersonFiaDetails :
                connectorSpaceAdPersonFiaDetailsListAdd) {

            try {

                log.info("(Dryrun : {}) Add OrganizationPerson : {}", serviceParameters.isDryrun(),
                        connectorSpaceAdPersonFiaDetails);

                byte[] unicodePwd = "".getBytes();

                if (StringUtils.isNotBlank(connectorSpaceAdPersonFiaDetails.getUnicodepwd())) {
//                    unicodePwd = kmsClientService.decrypt(connectorSpaceAdPersonExternalDetails.getCn(),
//                            connectorSpaceAdPersonExternalDetails.getUnicodepwd());
                    // 2024-11-12 pwd filter 攔截到的其實是sAMAccountName
                    unicodePwd = kmsClientService.decrypt(connectorSpaceAdPersonFiaDetails.getSamaccountname(),
                            connectorSpaceAdPersonFiaDetails.getUnicodepwd());
                } else {
                    // 不管怎樣先設組預設的給它
                    String password = "!QAZ2wsx3edc";
                    unicodePwd = DataUtils.toQuoteUnicode(password);
                }

                if (!serviceParameters.isDryrun()) {

                    LDAPResult result = activeDirectoryService.addOrganizationPerson(
                            //connectorSpaceAdPersonFiaDetails.getDn(),
                            "CN="+connectorSpaceAdPersonFiaDetails.getCn()+","+serviceParameters.getTargetdn(),
                            connectorSpaceAdPersonFiaDetails.getCn(),
                            connectorSpaceAdPersonFiaDetails.getEmployeeid(),
                            connectorSpaceAdPersonFiaDetails.getSn(),
                            connectorSpaceAdPersonFiaDetails.getOu(),
                            connectorSpaceAdPersonFiaDetails.getExtensionattribute1(),
                            connectorSpaceAdPersonFiaDetails.getExtensionattribute2(),
                            connectorSpaceAdPersonFiaDetails.getAccountexpires(),
                            //connectorSpaceAdPersonFiaDetails.getUserprincipalname(),
                            connectorSpaceAdPersonFiaDetails.getCn()+upndomain,
                            connectorSpaceAdPersonFiaDetails.getSamaccountname(),
                            connectorSpaceAdPersonFiaDetails.getTitle(),
                            connectorSpaceAdPersonFiaDetails.getDepartment(),
                            connectorSpaceAdPersonFiaDetails.getExtensionattribute10(),
                            connectorSpaceAdPersonFiaDetails.getExtensionattribute11(),
                            connectorSpaceAdPersonFiaDetails.getExtensionattribute12(),
                            connectorSpaceAdPersonFiaDetails.getExtensionattribute13(),
                            connectorSpaceAdPersonFiaDetails.getExtensionattribute14(),
                            connectorSpaceAdPersonFiaDetails.getExtensionattribute15(),
                            connectorSpaceAdPersonFiaDetails.getDisplayname(), unicodePwd,
                            connectorSpaceAdPersonFiaDetails.getUseraccountcontrol(),
                            connectorSpaceAdPersonFiaDetails.getUserparameters(),
                            connectorSpaceAdPersonFiaDetails.getPager());
                }
            } catch (Exception ex) {

                String stackTrace = ExceptionUtils.getStackTrace(ex);
                // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                log.error("{}", stackTrace);
                eventLogsService.record("trans", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                health = "N";
            }
        }

        // 需刪除(在資料庫找在cn清單中且被標記為刪除)
        List<ConnectorSpaceAdPersonFiaDetails> connectorSpaceAdPersonFiaDetailsListDelete = connectorSpaceAdPersonFiaDetailsRepository
                .findAllByCnInAndPlaceholderAndObjectguid(cnList, serviceParameters.getPlaceholder(),"-1");

        log.info("ActiveDirectory OrganizationPerson will be deleted list size: {}", connectorSpaceAdPersonFiaDetailsListDelete.size());

        for (ConnectorSpaceAdPersonFiaDetails connectorSpaceAdPersonFiaDetails :
                connectorSpaceAdPersonFiaDetailsListDelete) {

            log.info("(Dryrun : {}) Delete OrganizationPerson : {}", serviceParameters.isDryrun(),
                    connectorSpaceAdPersonFiaDetails);

            try {
                if (!serviceParameters.isDryrun()) {

                    // removing ad user
                    LDAPResult result = activeDirectoryService.
                            removeOrganizationPerson("CN="+connectorSpaceAdPersonFiaDetails.getCn()+","+serviceParameters.getTargetdn());

                    log.info("result = {}", result);
                }
            } catch (Exception ex) {

                String stackTrace = ExceptionUtils.getStackTrace(ex);
                // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                log.error("{}", stackTrace);
                eventLogsService.record("trans", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                health = "N";
            }
        }

        // 密碼需更新(從資料庫中找尋未標記刪除且24小時內有變更密碼的人)
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTimestamp);
        calendar.add(Calendar.DAY_OF_MONTH, -1);  // 减去 1 天
        Timestamp oneDayBefore = new Timestamp(calendar.getTimeInMillis());

        List<ConnectorSpaceAdPersonFiaDetails> connectorSpaceAdPersonFiaDetailsListPwdSync = connectorSpaceAdPersonFiaDetailsRepository
                .findAllByObjectguidNotAndPwdlastsetAfter("-1",oneDayBefore);

        log.info("ActiveDirectory OrganizationPerson pwd changed in one day list size: {}", connectorSpaceAdPersonFiaDetailsListPwdSync.size());

        for (ConnectorSpaceAdPersonFiaDetails connectorSpaceAdPersonFiaPwdSync :
                connectorSpaceAdPersonFiaDetailsListPwdSync) {

            if (connectorSpaceAdPersonFiaPwdSync.getPwdlastset().after(cnMap.get(connectorSpaceAdPersonFiaPwdSync.getCn()))) {
                log.info("(Dryrun : {}) PwdSync OrganizationPerson : {}", serviceParameters.isDryrun(),
                        connectorSpaceAdPersonFiaPwdSync);

                // 更新密碼
                try {
                    if (!serviceParameters.isDryrun()) {
                        // pwd filter攔載到的其實是 samAccountName
                        byte[] unicodePwd = kmsClientService.decrypt(connectorSpaceAdPersonFiaPwdSync.getSamaccountname(),
                                connectorSpaceAdPersonFiaPwdSync.getUnicodepwd());

                        LDAPResult result = activeDirectoryService.changeUserPassword(
                                "CN="+connectorSpaceAdPersonFiaPwdSync.getCn()+","+serviceParameters.getTargetdn(),
                                unicodePwd,
                                connectorSpaceAdPersonFiaPwdSync.getUnicodepwdHash(),
                                connectorSpaceAdPersonFiaPwdSync.getPwdlastset(),
                                cnMap.get(connectorSpaceAdPersonFiaPwdSync.getCn()));

                        log.info("result = {}", result);

                        result = activeDirectoryService
                                .unlockOrganizationPerson("CN="+connectorSpaceAdPersonFiaPwdSync.getCn()+","+serviceParameters.getTargetdn());

                        log.info("result = {}", result);
                    }
                } catch (Exception ex) {

                    String stackTrace = ExceptionUtils.getStackTrace(ex);
                    // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                    stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                    log.error("{}", stackTrace);
                    eventLogsService.record("trans", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                    health = "N";
                }
            }
        }
    }

}
