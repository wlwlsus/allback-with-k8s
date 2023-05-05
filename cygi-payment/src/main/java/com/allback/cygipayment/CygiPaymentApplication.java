package com.allback.cygipayment;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableFeignClients
@EnableDiscoveryClient
@EnableBatchProcessing
@EnableScheduling
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class CygiPaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(CygiPaymentApplication.class, args);
	}

}
