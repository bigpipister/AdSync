package com.cht.directory.connector.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "spring.ldap")
public class LDAPProperties {

    protected String host;

    protected int port;

    protected String username;

    protected boolean secure;

    protected String password;

    protected int poolSize;
}
