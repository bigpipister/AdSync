package com.cht.directory.connector.sync.config;

import com.cht.directory.connector.service.EdirectoryService;
import com.unboundid.ldap.sdk.LDAPConnectionPool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EdirectoryScanConfigurer {

    @ConditionalOnProperty(prefix = "spring.ldap2", value = "enabled",
            havingValue = "true", matchIfMissing = false)
    @Bean
    public EdirectoryService edirectoryService(@Qualifier("ldap2") LDAPConnectionPool ldapConnectionPool) {

        return new EdirectoryService(ldapConnectionPool);
    }
}
