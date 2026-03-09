package com.cht.directory.connector.sync.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cht.directory.connector.service.*;
import com.unboundid.ldap.sdk.LDAPConnectionPool;

@Configuration
public class DirectorySyncConfig {

    @Autowired
    @Bean
    public ActiveDirectoryService adDirectoryService(@Qualifier("ldap1")LDAPConnectionPool ldapConnectionPool) {

        return new ActiveDirectoryService(ldapConnectionPool);
    }

    @ConditionalOnProperty(prefix = "exchangeService", value = "enabled", havingValue = "true", matchIfMissing = false)
    @Bean
    public ExchangeService msExchangeService() {

        return new MsExchangeServiceImpl();
    }

    @ConditionalOnProperty(prefix = "exchangeService", value = "enabled", havingValue = "false", matchIfMissing = true)
    @Bean
    public ExchangeService dummyExchangeService() {

        return new DummyExchangeServiceImpl();
    }

//    @Bean
//    public AuditLogsService auditLogsService() {
//
//        return new AuditLogsService();
//    }
//
//    @Bean
//    public StatusLogsService statusLogsService() {
//
//        return new StatusLogsService();
//    }
//
//    @Bean
//    public EventLogsService eventLogsService() {
//
//        return new EventLogsService();
//    }
}
