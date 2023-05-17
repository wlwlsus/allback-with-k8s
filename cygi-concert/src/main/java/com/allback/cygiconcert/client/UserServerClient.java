package com.allback.cygiconcert.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-25
 * description :
 */

@FeignClient(name = "user-service", url = "${user.server.url}", path = "${user.server.prefix}")
public interface UserServerClient {

  @GetMapping("/{userId}")
  ResponseEntity<Void> checkUser(@PathVariable long userId);
}

