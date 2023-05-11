package com.allback.cygipayment.controller;


import com.allback.cygipayment.dto.request.KakaoPayApproveReqDto;
import com.allback.cygipayment.dto.request.KakaoPayReqDto;
import com.allback.cygipayment.dto.request.ReservationFillReqDto;
import com.allback.cygipayment.dto.response.KakaoPayApproveResDto;
import com.allback.cygipayment.dto.response.KakaoPayResDto;
import com.allback.cygipayment.dto.response.ReservationListResDto;
import com.allback.cygipayment.dto.response.ReservationResDto;
import com.allback.cygipayment.service.KakaoPayService;
import com.allback.cygipayment.service.ReservationService;
import com.allback.cygipayment.util.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "reservation", description = "예약 API")
@Slf4j
@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

	private final KakaoPayService kakaoPayService;
	private final ReservationService reservationService;

	@Operation(summary = "유저 예약 목록 조회")
	@GetMapping(value = "/id/{userId}")
	public ResponseEntity<?> getReservationList(
			@PathVariable long userId,
			@Schema(hidden = true)
			Pageable pageable,
			@Schema(description = "페이지 별 개수", example = "10")
			@RequestParam
			int size,
			@Schema(description = "페이지", example = "0")
			@RequestParam
			int page
	) {
		ReservationListResDto res = reservationService.getReservationList(userId, pageable);
		log.info("예약 목록 조회 데이터 : {}", res);
		return Response.makeResponse(HttpStatus.OK, "유저 예약 목록 조회 성공", res);
	}

//	@Operation(summary = "예약 세부 정보 조회")
//	@GetMapping(value = "/{reservationId}")
//	public ResponseEntity<?> getReservationInfo(@PathVariable long reservationId) {
//		ReservationResDto res = reservationService.getReservationById(reservationId);
//		log.info("예약 세부 정보 조회 : {}", res);
//		return Response.makeResponse(HttpStatus.OK, "예약 세부 정보 조회 성공", res);
//	}

	@Operation(summary = "환불")
	@PutMapping(value = "/refund/{reservationId}")
	public ResponseEntity<?> getReservationCancel(@PathVariable long reservationId) {
		reservationService.cancelReservation(reservationId);
		return Response.ok("환불 성공");
	}

	@Operation(summary = "예약")
	@PutMapping(value = "/{reservationId}")
	public ResponseEntity<Void> reservationAndPayment(@PathVariable long reservationId,
	                                               @RequestBody ReservationFillReqDto reservationFillReqDto) {
		reservationService.reserve(reservationId, reservationFillReqDto);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Operation(summary = "결제 준비")
	@PostMapping(value = "/charge")
	public ResponseEntity<?> payment(@RequestBody KakaoPayReqDto request) {
		KakaoPayResDto response = kakaoPayService.requestPayment(request);
		log.info("KakaoPayResDto : {}", response);
		return Response.makeResponse(HttpStatus.OK, "결제 준비 완료", response);
	}

	@Operation(summary = "결제 승인")
	@PostMapping("/approve/{userId}")
	public ResponseEntity<?> approvePayment(@PathVariable long userId, @RequestBody KakaoPayApproveReqDto requestDto) {
		KakaoPayApproveResDto response = kakaoPayService.approvePayment(userId, requestDto);
		log.info("KakaoPayApproveResDto : {}", requestDto);
		return Response.makeResponse(HttpStatus.OK, "결제 승인 완료", response);
	}
}
