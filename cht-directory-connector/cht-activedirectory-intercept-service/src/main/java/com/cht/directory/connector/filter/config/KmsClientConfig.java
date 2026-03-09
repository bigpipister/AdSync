package com.cht.directory.connector.filter.config;

import com.cht.directory.connector.security.KmsClientService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KmsClientConfig {

    @Bean
    public KmsClientService kmsClientService() {

        return new KmsClientService();
    }
}
