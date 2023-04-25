package com.allback.cygiuser.controller;

import com.allback.cygiuser.dto.request.RefundRequest;
import com.allback.cygiuser.entity.Users;
import com.allback.cygiuser.repository.UserRepository;
import com.allback.cygiuser.service.UserService;
import com.allback.cygiuser.util.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-25
 * description :
 */


@Tag(name = "refund", description = "환불 금액 정산 API")
@RestController
@Slf4j
@RequestMapping("/refund")
@RequiredArgsConstructor
public class RefundController {

	private final UserService userService;

	@PostMapping("")
	public ResponseEntity<?> refund(@RequestBody RefundRequest request) {
		userService.refund(request);
		return Response.makeResponse(HttpStatus.OK, "환불 금액 정산 성공");
	}
}
