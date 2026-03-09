package com.cht.directory.connector.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.cht.exchange.commander.ExCommander;
import com.cht.exchange.common.CmdResult;
import com.cht.exchange.pscmd.DisableMailbox;
import com.cht.exchange.pscmd.EnableDistributionGroup;
import com.cht.exchange.pscmd.EnableMailbox;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MsExchangeServiceImpl implements ExchangeService {

    @Autowired
    ExCommander exCommander;

    public void enableDistributionGroup(String groupName) throws Exception {

        EnableDistributionGroup enableDistributionGroup = new EnableDistributionGroup();

        enableDistributionGroup.setIdentity(groupName);

        CmdResult cmdResult = exCommander.enableDistributionGroup(enableDistributionGroup);

        log.info("execute command: {}", cmdResult.getCommand());
        log.info("command result status: {}", cmdResult.getStatus());
    }

    /**
     * 啟用信箱
     */
    public void enableMailBox(String account) {

        EnableMailbox enableMailbox = new EnableMailbox();
        enableMailbox.setIdentity(account);
        CmdResult cmdResult = exCommander.enableMailbox(enableMailbox);

        log.info("execute command: {}", cmdResult.getCommand());
        log.info("command result status: {}", cmdResult.getStatus());
    }

    /**
     * 停用信箱
     */
    public void disableMailBox(String account) {

        DisableMailbox disableMailbox = new DisableMailbox();
        disableMailbox.setIdentity(account);
        disableMailbox.setConfirm(false);
        CmdResult cmdResult = exCommander.disableMailbox(disableMailbox);

        log.info("execute command: {}", cmdResult.getCommand());
        log.info("command result status: {}", cmdResult.getStatus());
    }
}
