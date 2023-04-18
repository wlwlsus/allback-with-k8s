package com.allback.cygipayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CygiPaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(CygiPaymentApplication.class, args);
	}

}
