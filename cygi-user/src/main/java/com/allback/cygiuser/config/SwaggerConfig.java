package com.allback.cygiuser.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "🌟 CYGI User Service API 명세서 🌟",
        version = "v1.0.0",
        description = "CYGI User Service API 명세서"
    ),
    servers = {
        @Server(url = "/user-service/api/v1")
    }
)
public class SwaggerConfig {

  @Bean
  public GroupedOpenApi paymentServiceApi() {
    return GroupedOpenApi.builder()
        .group("user-service")
        .pathsToMatch("/**")
        .build();
  }

}