package com.cht.directory.connector.filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = { "com.cht.directory.connector.filter","com.cht.directory.connector.security" })
@EnableScheduling
public class AppApplication {

	public static void main(String[] args) {

		SpringApplication.run(AppApplication.class, args);
	}

}
