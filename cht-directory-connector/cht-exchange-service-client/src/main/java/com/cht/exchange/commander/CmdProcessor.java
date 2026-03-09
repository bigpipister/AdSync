package com.cht.exchange.commander;

import com.beust.jcommander.JCommander;
import com.cht.exchange.pscmd.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Service
@Slf4j
public class CmdProcessor {

    @Autowired
    RemoteCommander remoteCommander;

    public void proccess(String... args) {
        String[] params = Arrays.copyOfRange(args, 1, args.length);
        Map<String,String> response;
        try {
            switch(args[0]) {
                case "Get-MailboxDatabase":
                    GetMailboxDatabase getDatabaseParams = new GetMailboxDatabase();
                    new JCommander(getDatabaseParams).parse(params);
                    response = remoteCommander.getMailboxDatabase(getDatabaseParams);
                    log.info("execute command: " + response.get("command"));
                    log.info("is error: " + response.get("isError"));
                    log.info("command result: " + response.get("message"));
                    System.exit(0);
                case "Disable-Mailbox":
                    DisableMailbox disableParams = new DisableMailbox();
                    new JCommander(disableParams).parse(params);
                    response = remoteCommander.disableMailbox(disableParams);
                    log.info("execute command: " + response.get("command"));
                    log.info("is error: " + response.get("isError"));
                    log.info("command result: " + response.get("message"));
                    System.exit(0);
                case "Enable-Mailbox":
                    EnableMailbox enableParams = new EnableMailbox();
                    new JCommander(enableParams).parse(params);
                    response = remoteCommander.enableMailbox(enableParams);
                    log.info("execute command: " + response.get("command"));
                    log.info("is error: " + response.get("isError"));
                    log.info("command result: " + response.get("message"));
                    System.exit(0);
                case "Get-Mailbox":
                    GetMailbox getParams = new GetMailbox();
                    new JCommander(getParams).parse(params);
                    response = remoteCommander.getMailbox(getParams);
                    log.info("execute command: " + response.get("command"));
                    log.info("is error: " + response.get("isError"));
                    log.info("command result: " + response.get("message"));
                    System.exit(0);
                case "New-Mailbox":
                    NewMailbox newParams = new NewMailbox();
                    new JCommander(newParams).parse(params);
                    response = remoteCommander.newMailbox(newParams);
                    log.info("execute command: " + response.get("command"));
                    log.info("is error: " + response.get("isError"));
                    log.info("command result: " + response.get("message"));
                    System.exit(0);
                case "Remove-Mailbox":
                    RemoveMailbox removeParams = new RemoveMailbox();
                    new JCommander(removeParams).parse(params);
                    response = remoteCommander.removeMailbox(removeParams);
                    log.info("execute command: " + response.get("command"));
                    log.info("is error: " + response.get("isError"));
                    log.info("command result: " + response.get("message"));
                    System.exit(0);
                case "Set-Mailbox":
                    SetMailbox setParams = new SetMailbox();
                    new JCommander(setParams).parse(params);
                    response = remoteCommander.setMailbox(setParams);
                    log.info("execute command: " + response.get("command"));
                    log.info("is error: " + response.get("isError"));
                    log.info("command result: " + response.get("message"));
                    System.exit(0);
                case "Get-DistributionGroup":
                    GetDistributionGroup getGroupParams = new GetDistributionGroup();
                    new JCommander(getGroupParams).parse(params);
                    response = remoteCommander.getDistributionGroup(getGroupParams);
                    log.info("execute command: " + response.get("command"));
                    log.info("is error: " + response.get("isError"));
                    log.info("command result: " + response.get("message"));
                    System.exit(0);
                case "Get-DistributionGroupMember":
                    GetDistributionGroupMember getGroupMemberParams = new GetDistributionGroupMember();
                    new JCommander(getGroupMemberParams).parse(params);
                    response = remoteCommander.getDistributionGroupMember(getGroupMemberParams);
                    log.info("execute command: " + response.get("command"));
                    log.info("is error: " + response.get("isError"));
                    log.info("command result: " + response.get("message"));
                    System.exit(0);
                case "Enable-DistributionGroup":
                    EnableDistributionGroup enableGroupParams = new EnableDistributionGroup();
                    new JCommander(enableGroupParams).parse(params);
                    response = remoteCommander.enableDistributionGroup(enableGroupParams);
                    log.info("execute command: " + response.get("command"));
                    log.info("is error: " + response.get("isError"));
                    log.info("command result: " + response.get("message"));
                    System.exit(0);
                case "Disable-DistributionGroup":
                    DisableDistributionGroup disableGroupParams = new DisableDistributionGroup();
                    new JCommander(disableGroupParams).parse(params);
                    response = remoteCommander.disableDistributionGroup(disableGroupParams);
                    log.info("execute command: " + response.get("command"));
                    log.info("is error: " + response.get("isError"));
                    log.info("command result: " + response.get("message"));
                    System.exit(0);
                default:
                    log.error("command not support: " + args[1]);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        System.exit(0);
    }
}
