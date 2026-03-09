package com.cht.directory.connector.service.config;

import java.security.GeneralSecurityException;

import javax.net.ssl.SSLSocketFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.cht.directory.connector.security.FakeX509TrustManager;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionPool;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.util.ssl.SSLUtil;

@Configuration
public class LDAPConfigurer {

    @Autowired
    private LDAPProperties ldapProperties;

    @Autowired
    private LDAP2Properties ldap2Properties;

    @Primary
    @ConditionalOnProperty(prefix = "spring", name = "ldap.host")
    @Bean(name = "ldap1")
    public LDAPConnectionPool connectionPool() throws LDAPException, GeneralSecurityException {

        LDAPConnection connection = null;

        if (ldapProperties.secure) {

            SSLUtil sslUtil = new SSLUtil(new FakeX509TrustManager());
            SSLSocketFactory socketFactory = sslUtil.createSSLSocketFactory();
            connection = new LDAPConnection(socketFactory, ldapProperties.host,
                    ldapProperties.port);
        } else
            connection = new LDAPConnection(ldapProperties.host, ldapProperties.port);

        connection.bind(ldapProperties.username, ldapProperties.password);

        LDAPConnectionPool connectionPool = new LDAPConnectionPool(connection,
                ldapProperties.getPoolSize());

        connectionPool.setRetryFailedOperationsDueToInvalidConnections(true);

        return connectionPool;
    }

    @ConditionalOnProperty(prefix = "spring.ldap2", value = "enabled",
                            havingValue = "true", matchIfMissing = false)
    @Bean(name = "ldap2")
    public LDAPConnectionPool connectionPool2() throws LDAPException, GeneralSecurityException {

        LDAPConnection connection = null;

        if (ldap2Properties.secure) {

            SSLUtil sslUtil = new SSLUtil(new FakeX509TrustManager());
            SSLSocketFactory socketFactory = sslUtil.createSSLSocketFactory();
            connection = new LDAPConnection(socketFactory, ldap2Properties.host,
                    ldap2Properties.port);
        } else
            connection = new LDAPConnection(ldap2Properties.host, ldap2Properties.port);

        connection.bind(ldap2Properties.username, ldap2Properties.password);

        LDAPConnectionPool connectionPool = new LDAPConnectionPool(connection,
                ldap2Properties.poolSize);

        connectionPool.setRetryFailedOperationsDueToInvalidConnections(true);

        return connectionPool;
    }
}
