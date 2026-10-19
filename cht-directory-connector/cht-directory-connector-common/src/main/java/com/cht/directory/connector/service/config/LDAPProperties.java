package com.cht.directory.connector.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "spring.ldap")
public class LDAPProperties {

    // 可設定多台，以逗號分隔，排前面的優先 (ex: 10.22.242.39,10.22.242.40)
    protected String host;

    protected int port;

    protected String username;

    protected boolean secure;

    protected String password;

    protected int poolSize;

    // 連線逾時 (毫秒)，逾時即換下一台
    protected int connectTimeoutMs = 5000;
}
