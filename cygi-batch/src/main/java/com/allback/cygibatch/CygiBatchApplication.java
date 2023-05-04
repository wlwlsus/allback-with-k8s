package com.allback.cygibatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableFeignClients
@EnableBatchProcessing
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class CygiBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(CygiBatchApplication.class, args);
	}

}
