package com.allback.cygiuser.controller;

import com.allback.cygiuser.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "user - payment", description = "결제 서버와 통신하는 API")
@RestController
@Slf4j
@RequestMapping("/server-payment")
@RequiredArgsConstructor
public class PaymentServerController {
    private final UserServiceImpl userService;
    @PutMapping("/point")
    ResponseEntity<Void> sendPoint(@RequestParam long receiverId, @RequestParam long point){
        userService.updateCash(receiverId, point);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
