package com.allback.cygipayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class CygiPaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(CygiPaymentApplication.class, args);
	}

}
