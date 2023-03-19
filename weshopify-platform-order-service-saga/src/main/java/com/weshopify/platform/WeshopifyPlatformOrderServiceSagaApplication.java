package com.weshopify.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class WeshopifyPlatformOrderServiceSagaApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeshopifyPlatformOrderServiceSagaApplication.class, args);
	}

	@Bean
	RestTemplate restTemplet() {
		return new RestTemplate();
	}
}
