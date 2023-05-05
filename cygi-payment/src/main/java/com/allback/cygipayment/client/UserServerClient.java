package com.allback.cygipayment.client;

import com.allback.cygipayment.dto.request.AmountReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-25
 * description :
 */

//@FeignClient(name = "user-server", url = "${user.server.url}", path = "${user.server.prefix}")
@FeignClient("user-service")
public interface UserServerClient {
  @PutMapping("/amount")
  void amount(@RequestBody AmountReqDto request);

  @PutMapping("/deduct/{userId}")
  void deductUserCash(@PathVariable(value = "userId") long userId, @RequestParam(name = "price") int price);

  @PostMapping("/balace")
  ResponseEntity<Void> getEndedConcert();

  @PostMapping("/point")
  ResponseEntity<Void> sendPoint(@RequestParam long receiverId, @RequestParam long point);

  @GetMapping("/")
  ResponseEntity<String> testPing();
}

