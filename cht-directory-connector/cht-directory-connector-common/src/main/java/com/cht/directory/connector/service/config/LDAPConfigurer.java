package com.cht.directory.connector.service.config;

import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.cht.directory.connector.security.FakeX509TrustManager;
import com.unboundid.ldap.sdk.FailoverServerSet;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionOptions;
import com.unboundid.ldap.sdk.LDAPConnectionPool;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SimpleBindRequest;
import com.unboundid.util.ssl.SSLUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

        // host 可設定多台 (逗號分隔)，依序嘗試，第一台連不上才換下一台
        String[] hosts = Arrays.stream(ldapProperties.host.split(","))
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .toArray(String[]::new);
        int[] ports = new int[hosts.length];
        Arrays.fill(ports, ldapProperties.port);

        LDAPConnectionOptions connectionOptions = new LDAPConnectionOptions();
        connectionOptions.setConnectTimeoutMillis(ldapProperties.connectTimeoutMs);

        SocketFactory socketFactory = null;

        if (ldapProperties.secure) {

            SSLUtil sslUtil = new SSLUtil(new FakeX509TrustManager());
            socketFactory = sslUtil.createSSLSocketFactory();
        }

        // 不使用 RoundRobin，避免同一輪作業分散到不同 DC 而受 AD 複寫延遲影響
        FailoverServerSet serverSet = new FailoverServerSet(hosts, ports, socketFactory,
                connectionOptions);
        SimpleBindRequest bindRequest = new SimpleBindRequest(ldapProperties.username,
                ldapProperties.password);

        LDAPConnectionPool connectionPool = new LDAPConnectionPool(serverSet, bindRequest, 1,
                Math.max(1, ldapProperties.getPoolSize()));

        connectionPool.setRetryFailedOperationsDueToInvalidConnections(true);

        LDAPConnection connection = connectionPool.getConnection();
        log.info("LDAP hosts : {}, connected to : {}:{}", Arrays.toString(hosts),
                connection.getConnectedAddress(), connection.getConnectedPort());
        connectionPool.releaseConnection(connection);

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
