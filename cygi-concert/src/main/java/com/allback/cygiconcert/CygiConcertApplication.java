package com.allback.cygiconcert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableFeignClients
@EnableJpaAuditing
@EnableDiscoveryClient
@SpringBootApplication
public class CygiConcertApplication {

    public static void main(String[] args) {
        SpringApplication.run(CygiConcertApplication.class, args);
    }

}


