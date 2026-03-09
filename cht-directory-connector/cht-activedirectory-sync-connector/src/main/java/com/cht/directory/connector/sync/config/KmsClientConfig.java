package com.cht.directory.connector.sync.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cht.directory.connector.security.KmsClientService;

@Configuration
public class KmsClientConfig {

    @Bean
    public KmsClientService kmsClientService() {

        return new KmsClientService();
    }
}
