package com.cht.exchange.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.cht.exchange.commander, com.cht.exchange.security")
public class CommanderConfiguration {
}
