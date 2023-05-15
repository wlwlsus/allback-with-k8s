package com.allback.cygiuser.controller;

import com.allback.cygiuser.dto.request.AmountRequest;
import com.allback.cygiuser.service.UserServiceImpl;
import com.allback.cygiuser.util.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "user - payment", description = "결제 서버와 통신하는 API")
@RestController
@Slf4j
@RequestMapping("/server-payment")
@RequiredArgsConstructor
public class PaymentServerController {

  private final UserServiceImpl userService;

  @PutMapping("/amount")
  public ResponseEntity<?> amount(@RequestBody AmountRequest request) {
    userService.amount(request);
    return Response.makeResponse(HttpStatus.OK, "정산 성공");
  }

  @PutMapping("/deduct/{userId}")
  public ResponseEntity<?> deductUserCash(@PathVariable long userId, @RequestParam int price) {
    userService.deductUserCash(userId, price);
    return Response.makeResponse(HttpStatus.OK, "예약 결제 성공");
  }

  @PutMapping("/point")
  ResponseEntity<Void> sendPoint(@RequestParam long receiverId, @RequestParam long point) {
    userService.updateCash(receiverId, point);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/ping")
  public String ping(){
    return "pong!";
  }
}
