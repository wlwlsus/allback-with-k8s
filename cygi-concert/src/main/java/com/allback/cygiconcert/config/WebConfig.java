package com.allback.cygiconcert.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000",  "http://localhost:8002")
            .allowedMethods("*")
            .allowCredentials(true) // 자격증명 허용
            .allowedHeaders("*")
            .exposedHeaders("*")
            .maxAge(3600);
    }
}