package com.allback.cygiconcert.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-server", url = "${user.server.url}", path = "${user.server.prefix}")
@Component
public interface UserServerClient {

    @GetMapping("/check/{userId}")
    boolean checkUserId(@PathVariable long userId);
}
