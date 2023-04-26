package com.allback.cygiconcert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CygiConcertApplication {

	public static void main(String[] args) {
		SpringApplication.run(CygiConcertApplication.class, args);
	}

}
