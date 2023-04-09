package com.weshopify.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;

@SpringBootApplication
public class WeshopifyUserManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeshopifyUserManagementServiceApplication.class, args);
	}

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    ObjectMapper objectMapper() {
    	return new ObjectMapper();
    }
    
    /**
     * for recording custom metrics by the prometheous server
     * @param registry
     * @return
     */
    @Bean
	public TimedAspect timedAspect(MeterRegistry registry) {
	    return new TimedAspect(registry);
	}
    
   
}
