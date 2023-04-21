package com.allback.cygipayment.controller;


import com.allback.cygipayment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "reservation", description = "예약 API")
@Slf4j
@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;

  @Operation(summary = "예약 목록 조회")
  @GetMapping(value = "")
  public ResponseEntity<?> getReservationList(
      @PageableDefault(size = 10, page = 0)
      Pageable pageable
  ) {
    return null;
  }

  @Operation(summary = "예약 세부 정보 조회")
  @GetMapping(value = "/{reservationId}")
  public ResponseEntity<?> getReservationInfo(
      @PathVariable String reservationId) {
    return null;
  }

  @Operation(summary = "예약 취소 및 환불")
  @PutMapping(value = "/refund/{reservationId}")
  public ResponseEntity<?> getReservationRefund(
      @PathVariable String reservationId) {
    return null;
  }

  @Operation(summary = "예약 및 결제")
  @PutMapping(value = "/{reservationId}")
  public ResponseEntity<?> reservationAndPayment(
      @PathVariable String reservationId) {
    return null;
  }

  @Operation(summary = "충전")
  @PutMapping(value = "/account")
  public ResponseEntity<?> payment() {
    return null;
  }


}
