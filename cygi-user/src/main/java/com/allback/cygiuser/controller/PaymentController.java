package com.allback.cygiuser.controller;

import com.allback.cygiuser.dto.request.AmountRequest;
import com.allback.cygiuser.service.UserService;
import com.allback.cygiuser.util.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-25
 * description :
 */


@Tag(name = "refund", description = "환불 금액 정산 API")
@RestController
@Slf4j
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

	private final UserService userService;

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
}
