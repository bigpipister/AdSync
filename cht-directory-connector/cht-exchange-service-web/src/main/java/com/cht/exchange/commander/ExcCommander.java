package com.cht.exchange.commander;

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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class ExcCommander {

    //@Autowired
    //private ExecutorService executorService;

    @Value("${server.logRegion:inner}")
    private String region;

    @Autowired
    private AuditLogsRepository auditService;

    public void runTask(List<String> commands) {
        //executorService.execute(new Runnable() {
            //public void run() {
                try {
                    long startTime = System.currentTimeMillis();

                    // 取得本機 fqdn
                    InetAddress localhost = InetAddress.getLocalHost();
                    String fqdnName = localhost.getCanonicalHostName();

                    // default power shell 無法直接執行 exchange command
                    // 所以需要用下列指令先進入一個 exchange ps session
                    String openSessionString = "Enter-PSSession -ConfigurationName microsoft.exchange -ConnectionUri http://" + fqdnName + "/powershell";
                    Map<String, String> myConfig = new HashMap<>();
                    myConfig.put("maxWait", "10000");
                    PowerShell powerShell = PowerShell.openSession().configuration(myConfig);
                    powerShell.executeCommand(openSessionString);
                    Thread.sleep(3000);
                    // 進入 exchange ps session 後先執一個最基本的 command 確認是否有成功
                    PowerShellResponse response = powerShell.executeCommand("Get-MailboxDatabase");
                    if (response.isError()) {
                        // 失敗了，等下次排程呼叫吧
                        log.info("Enter PSSession fail! Wait for next schedule!");
                    } else {
                        for (String command : commands) {
                            // 準備log
                            AuditLogs auditLog = new AuditLogs();
                            auditLog.setRegion(region);
                            auditLog.setCategory("Other");
                            auditLog.setActivitydisplayname("Exchange command service");
                            auditLog.setCorrelationid("exchange");
                            auditLog.setInitiatedby("cht-directorysync-toad-connector");
                            auditLog.setLoggedbyservice("exchange-cmd-service");
                            auditLog.setResultcode(999);
                            auditLog.setTargetresources(command);
                            auditLog.setActivitydatetime(new Timestamp(new Date().getTime()));

                            response = powerShell.executeCommand(command);
                            long endTime = System.currentTimeMillis();
                            if (response.isError()) {
                                // 失敗了，等下次排程呼叫吧
                                log.info("Execute-Command: " + command + ", result: fail!");
                                auditLog.setResult("failure");
                                auditLog.setAdditionaldetails("Execute-Command: " + command + ", result: fail!");
                            } else {
                                log.info("Execute-Command: " + command + ", result: success!");
                                auditLog.setResult("success");
                                auditLog.setAdditionaldetails("Execute-Command: " + command + ", result: success!");
                            }
                            auditLog.setDurationinmilliseconds(endTime - startTime);

                            // 寫入log
                            try {
                                auditService.save(auditLog);
                            } catch (Exception e) {
                                e.printStackTrace();
                                log.error("write log to db fail!");
                            }
                        }
                        powerShell.executeCommand("Exit-PSSession");
                    }
                    powerShell.close();
                } catch(Exception e) {
                    log.error("PSSession-Execute: " + e.getMessage());
                }
            //}
        //});
    }
}
