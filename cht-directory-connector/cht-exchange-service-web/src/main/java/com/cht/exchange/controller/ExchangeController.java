package com.cht.exchange.controller;

import com.cht.exchange.common.Utils;
import com.cht.exchange.queue.Producer;

import com.cht.exchange.queue.Workspace;
import com.cht.exchange.service.ExchangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/exchange")
@Slf4j
public class ExchangeController {
    //@Autowired
    //private Producer producer;

    @Autowired
    private Workspace workspace;

    @Autowired
    private ExchangeService exService;

    /**
     * 將 mailbox 的PowerShell指令送至queue內
     * @param  varPath  PowerShell的執行指令
     * @param  requestBody  PowerShell的執行參數
     * @return 回傳狀態
     */
    @PostMapping(
            value = "/queue/{command}",
            consumes="application/json;charset=UTF-8")
    public @ResponseBody  Map<String, Object> queueCommand(@PathVariable("command") String varPath,
                                             @RequestBody LinkedHashMap<String, Object> requestBody) {
        log.debug("original json : " + requestBody);
        // 將傳進來的 json 轉成 exchange command 對應的參數與參數值
        LinkedHashMap<String, String> paramMap = Utils.jsonFormatter(requestBody);
        log.debug("formatted json : " + paramMap);

        // 往embedded exchange.command.queue發送更新message
        // Destination destination = new ActiveMQQueue("exchange.command.queue");
        String command = "";
        if (varPath.equalsIgnoreCase("getMailboxDatabase")) {
            command = "Get-MailboxDatabase";
        } else if (varPath.equalsIgnoreCase("enableMailbox")) {
            command = "Enable-Mailbox";
        } else if (varPath.equalsIgnoreCase("disableMailbox")) {
            command = "Disable-Mailbox";
        } else if (varPath.equalsIgnoreCase("newMailbox")) {
            command = "Get-Mailbox";
        } else if (varPath.equalsIgnoreCase("getMailbox")) {
            command = "New-Mailbox";
        } else if (varPath.equalsIgnoreCase("removeMailbox")) {
            command = "Remove-Mailbox";
        } else if (varPath.equalsIgnoreCase("setMailbox")) {
            command = "Set-Mailbox";
        } else if (varPath.equalsIgnoreCase("enableDistributionGroup")) {
            command = "Enable-DistributionGroup";
        } else if (varPath.equalsIgnoreCase("getDistributionGroup")) {
            command = "Get-DistributionGroup";
        } else if (varPath.equalsIgnoreCase("disableDistributionGroup")) {
            command = "Disable-DistributionGroup";
        } else if (varPath.equalsIgnoreCase("getDistributionGroupMember")) {
            command = "Get-DistributionGroupMember";
        } else {
            LinkedHashMap<String, Object> response = new LinkedHashMap();

            // 將指令與指令參數轉成真正要對 power shell 下的command
            response.put("command", Utils.genCommand(command, paramMap));
            response.put("status", HttpStatus.BAD_REQUEST.value());
            return response;
        }
        // 將指令與指令參數轉成真正要對 power shell 下的command
        String message = Utils.genCommand(command, paramMap);
        log.info("Queue In: " + message);
        //producer.sendMessage(destination, message);
        workspace.catchCommand(message);

        LinkedHashMap<String, Object> response = new LinkedHashMap();
        response.put("command", message);
        response.put("status", HttpStatus.OK.value());
        return response;
    }

    /**
     * 執行 mailbox 的PowerShell指令執行API
     * @param  varPath  PowerShell的執行指令
     * @param  requestBody  PowerShell的執行參數，格式為json
     * @return 回傳PowerShell的執行結果
     */
    @PostMapping(
            value = "/api/{command}",
            consumes="application/json;charset=UTF-8",
            produces="application/json;charset=UTF-8")
    public @ResponseBody  Map<String, String> executeCommand(@PathVariable("command") String varPath,
                                                                 @RequestBody LinkedHashMap<String, Object> requestBody) {
        log.debug("original json : " + requestBody);
        LinkedHashMap<String, String> paramMap = Utils.jsonFormatter(requestBody);
        log.debug("formatted json : " + paramMap);

        String command = "";
        if (varPath.equalsIgnoreCase("getMailboxDatabase")) {
            command = "Get-MailboxDatabase";
        } else if (varPath.equalsIgnoreCase("enableMailbox")) {
            command = "Enable-Mailbox";
        } else if (varPath.equalsIgnoreCase("disableMailbox")) {
            command = "Disable-Mailbox";
        } else if (varPath.equalsIgnoreCase("newMailbox")) {
            command = "Get-Mailbox";
        } else if (varPath.equalsIgnoreCase("getMailbox")) {
            command = "New-Mailbox";
        } else if (varPath.equalsIgnoreCase("removeMailbox")) {
            command = "Remove-Mailbox";
        } else if (varPath.equalsIgnoreCase("setMailbox")) {
            command = "Set-Mailbox";
        } else if (varPath.equalsIgnoreCase("enableDistributionGroup")) {
            command = "Enable-DistributionGroup";
        } else if (varPath.equalsIgnoreCase("getDistributionGroup")) {
            command = "Get-DistributionGroup";
        } else if (varPath.equalsIgnoreCase("disableDistributionGroup")) {
            command = "Disable-DistributionGroup";
        } else if (varPath.equalsIgnoreCase("getDistributionGroupMember")) {
            command = "Get-DistributionGroupMember";
        } else {
            LinkedHashMap<String, String> response = new LinkedHashMap();
            response.put("command", Utils.genCommand(command, paramMap));
            response.put("isError", "true");
            response.put("message", "command not acceptable");
            return response;
        }
        String message = Utils.genCommand(command, paramMap);
        log.info("API: " + message);

        LinkedHashMap<String, String> response = new LinkedHashMap();
        Map<String,String> cmdRes = exService.executePS(command, paramMap);
        response.put("command", cmdRes.get("command"));
        response.put("isError", cmdRes.get("isError"));
        response.put("message", cmdRes.get("message"));
        return response;
    }
}
