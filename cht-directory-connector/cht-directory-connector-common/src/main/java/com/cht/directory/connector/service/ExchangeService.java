package com.cht.directory.connector.service;

public interface ExchangeService {

    public void enableDistributionGroup(String groupName) throws Exception;

    public void enableMailBox(String account);

    public void disableMailBox(String account);
}
