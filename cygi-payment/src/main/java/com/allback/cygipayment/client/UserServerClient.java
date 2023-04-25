package com.allback.cygipayment.client;

import com.allback.cygipayment.dto.request.RefundRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-25
 * description :
 */

@FeignClient(name = "user-server", url = "${user.server.url}", path = "${user.server.prefix}")
public interface UserServerClient {
  @PutMapping("/refund")
  void refund(@RequestBody RefundRequest request);

  @PutMapping("/deduct/{userId}")
  void deductUserCash(@PathVariable(value = "userId") long userId, @RequestParam int price);
}

