package com.cht.directory.connector.sync.service;

import com.cht.directory.connector.security.utils.DataUtils;
import com.cht.directory.connector.service.ActiveDirectoryService;
import com.cht.directory.connector.service.EventLogsService;
import com.cht.directory.connector.service.StatusLogsService;
import com.unboundid.ldap.sdk.LDAPResult;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cht.directory.connector.sync.config.ServiceParameters;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.Date;

@Component
@Slf4j
public class ActivedirectorySyncService {

    @Autowired
    private ServiceParameters serviceParameters;

    @Autowired
    private OrganizationUnitSyncService organizationUnitSyncService;

    @Autowired
    private OrganizationPersonSyncService organizationPersonSyncService;

    @Autowired
    private GroupSyncService groupSyncService;

    @Autowired
    private ActiveDirectoryService adDirectoryService;

    @Autowired
    private StatusLogsService statusLogsService;

    @Autowired
    private EventLogsService eventLogsService;

    private String health = "Y";

    public void executeSync() throws Exception {

        log.info("ServiceParameters : {}", serviceParameters);

        Boolean running = true;
        health = "Y";
        Date date = new Date();
        Timestamp startDateTime = new Timestamp(date.getTime());
        long startTime = System.currentTimeMillis(); // 取得開始時間 (毫秒)
        statusLogsService.record("sync", serviceParameters.getBasedn(), "同步AD資料",
                startDateTime, running, health, 0);

        try {
            eventLogsService.del("sync", serviceParameters.getBasedn());

            if (serviceParameters.getScope() == null) {

                log.info("Starting all scope sync ...");

                // 組織整個異動時會有ou跟person的change(不會有modify，modify是指dn沒有變更的情況)，
                // ou change後person的change一定會失敗是正常的，等下一輪scan後db內的person dn就會被校正
                health = organizationUnitSyncService.executeSync();
                health = groupSyncService.executeSync();
                health = organizationPersonSyncService.executeSync();

                // HE: exception log 測試
                //throw new Exception("GroupSyncService excpeiton test");
            } else {

                if (serviceParameters.getScope().contains("1")) {
                    log.info("Starting organizationUnit scope sync ...");
                    health = organizationUnitSyncService.executeSync();
                }

                if (serviceParameters.getScope().contains("2")) {
                    log.info("Starting organizationPerson scope sync ...");
                    health = organizationPersonSyncService.executeSync();
                }

                if (serviceParameters.getScope().contains("3")) {
                    log.info("Starting group scope sync ...");
                    health = groupSyncService.executeSync();
                }
            }
        } catch (Exception ex) {

            String stackTrace = ExceptionUtils.getStackTrace(ex);
            // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
            stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
            log.error("{}", stackTrace);
            eventLogsService.record("sync", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
            health = "N";
        }

        running = false;
        long endTime = System.currentTimeMillis(); // 取得結束時間 (毫秒)
        long duration = (endTime - startTime) / 1000; // 轉換為秒數
        statusLogsService.record("sync", serviceParameters.getBasedn(), "同步AD資料",
                startDateTime, running, health, duration);

    }

    // sample
    public void addOrganizationPerson() throws Exception {
        String password = "Aa12\\34/56!78";
        byte[] unicodePwd = DataUtils.toQuoteUnicode(password);

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        LDAPResult addResult = adDirectoryService.addOrganizationPerson(
                "CN=user1,OU=Z011A00,OU=Z01,OU=B01ROOT,OU=MOF,DC=intbca,DC=gov,DC=tw",
                "user1", "NB99999", "user1", "Z011A00",
                "1", "0", timestamp,
                "user1@intbca.gov.tw", "user1",
                "user1", "Z011A00", null,
                null, null, null,
                null, null, "use1(NB99999)",
                unicodePwd, 512, "ACTIVE", "1");

    }

    // sample
    public void addGroup() throws Exception {
        LDAPResult addResult = adDirectoryService.addGroup(
                "CN=B01ALL,OU=B01ROOT,OU=MOF,DC=entbca,DC=gov,DC=tw",
                "B01ALL", "2147483656", "B01ALL中區國稅局(所有員工)",
                "B01ALL中區國稅局(所有員工)","B01ALL中區國稅局(所有員工)","B01ALL中區國稅局(所有員工)",
                "B01ALL中區國稅局(所有員工)","B01ALL中區國稅局(所有員工)","B01ALL中區國稅局(所有員工)");

    }
}
