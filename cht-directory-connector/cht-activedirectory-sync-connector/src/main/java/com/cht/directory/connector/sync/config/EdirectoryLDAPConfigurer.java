package com.cht.directory.connector.sync.config;

import com.cht.directory.connector.security.FakeX509TrustManager;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPJSSESecureSocketFactory;
import com.unboundid.util.ssl.SSLUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLSocketFactory;

@ConditionalOnProperty(prefix = "spring.ldap2", value = "enabled",
        havingValue = "true", matchIfMissing = false)
@Configuration
public class EdirectoryLDAPConfigurer {

    @Value("${spring.ldap.host}")
    protected String host;

    @Value("${spring.ldap.port}")
    protected int port;

    @Value("${spring.ldap.username}")
    protected String username;

    @Value("${spring.ldap.secure:false}")
    protected boolean secure;

    @Value("${spring.ldap.password}")
    protected String password;

    @Bean
    public LDAPConnection ldapConnection() throws Exception {

        SSLUtil sslUtil = new SSLUtil(new FakeX509TrustManager());
        SSLSocketFactory socketFactory = sslUtil.createSSLSocketFactory();

        LDAPConnection ldapConnection = null;

        if (secure)
            ldapConnection = new LDAPConnection(new LDAPJSSESecureSocketFactory(socketFactory));
        else
            ldapConnection = new LDAPConnection();

        ldapConnection.connect(host, port);
        ldapConnection.bind(3, username, password.getBytes());

        return ldapConnection;
    }
}
