package com.cht.exchange.service;

import com.cht.exchange.common.Utils;
import com.cht.exchange.log.entity.AuditLogs;
import com.cht.exchange.log.repository.AuditLogsRepository;
import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ExchangeService {

    @Value("${server.logRegion:inner}")
    private String region;

    @Autowired
    AuditLogsRepository auditService;

    /**
     *執行PowerShell指令
     * @param  command  PowerShell指令名稱
     * @param params PowerShell指令參數
     * @return 回傳PowerShell的執行結果
     */
    public Map<String,String> executePS(String command, Map<String,String> params)  {
        String executeString = Utils.genCommand(command, params);
        // 準備回傳的訊息
        Map<String,String> response = new HashMap<String,String>();
        response.put("command", executeString);
        response.put("isError", "false");
        response.put("message", "");
        // 準備log
        AuditLogs auditLog = new AuditLogs();
        auditLog.setRegion(region);
        auditLog.setCategory("Other");
        auditLog.setActivitydisplayname("Exchange command service");
        auditLog.setCorrelationid("exchange");
        auditLog.setInitiatedby("exchange-service-client tool");
        auditLog.setLoggedbyservice("exchange-cmd-service");
        auditLog.setResultcode(999);
        auditLog.setTargetresources(executeString);
        auditLog.setActivitydatetime(new Timestamp(new Date().getTime()));
        long startTime = System.currentTimeMillis();
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            String fqdnName = localhost.getCanonicalHostName();
            String openSessionString = "Enter-PSSession -ConfigurationName microsoft.exchange -ConnectionUri http://" + fqdnName + "/powershell";
            Map<String, String> myConfig = new HashMap<>();
            myConfig.put("maxWait", "10000");
            PowerShell powerShell = PowerShell.openSession().configuration(myConfig);
            powerShell.executeCommand(openSessionString);
            Thread.sleep(3000);
            PowerShellResponse cmdRes = powerShell.executeCommand(executeString);
            log.info("Api:Process command: " + executeString + ", is error: " + cmdRes.isError());
            long endTime = System.currentTimeMillis();

            auditLog.setResult(cmdRes.isError() == true ? "failure" : "success");
            auditLog.setAdditionaldetails(cmdRes.getCommandOutput());
            auditLog.setDurationinmilliseconds(endTime - startTime);
            if (cmdRes.isError()) {
                auditLog.setResultreason(cmdRes.getCommandOutput().substring(cmdRes.getCommandOutput().indexOf("FullyQualifiedErrorId")));
                response.put("isError", "true");
                response.put("message", cmdRes.getCommandOutput().substring(cmdRes.getCommandOutput().indexOf("FullyQualifiedErrorId")));
            } else {
                response.put("isError", "false");
                response.put("message", cmdRes.getCommandOutput());
            }
            // 寫入log
            try {
                auditService.save(auditLog);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("write log to db fail!");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return response;
    }
}
