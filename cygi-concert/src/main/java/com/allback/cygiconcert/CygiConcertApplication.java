package com.allback.cygiconcert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class CygiConcertApplication {

	public static void main(String[] args) {
		SpringApplication.run(CygiConcertApplication.class, args);
	}

}
