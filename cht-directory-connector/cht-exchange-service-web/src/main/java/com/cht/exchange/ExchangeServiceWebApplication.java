package com.cht.exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExchangeServiceWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeServiceWebApplication.class, args);
	}

}
