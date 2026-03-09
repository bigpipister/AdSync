package com.cht.directory.connector.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DummyExchangeServiceImpl implements ExchangeService {

    @Override
    public void enableDistributionGroup(String groupName) throws Exception {

        log.info("dummy enableDistributionGroup : {}", groupName);
    }

    @Override
    public void enableMailBox(String account) {

        log.info("dummy enableMailBox : {}", account);
    }

    @Override
    public void disableMailBox(String account) {

        log.info("dummy disableMailBox : {}", account);
    }
}
