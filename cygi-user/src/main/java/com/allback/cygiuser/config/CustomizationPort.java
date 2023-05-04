package com.allback.cygiuser.config;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CustomizationPort implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

  @Override
  public void customize(ConfigurableServletWebServerFactory server) {
    Random random = new Random();
    var port = random.ints(8000, 8001)
        .findFirst()
        .getAsInt();

    server.setPort(port);
  }
}
