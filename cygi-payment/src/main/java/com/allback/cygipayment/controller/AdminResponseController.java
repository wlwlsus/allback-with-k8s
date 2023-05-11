package com.allback.cygipayment.controller;

import com.allback.cygipayment.dto.response.ReservationListResAllDto;
import com.allback.cygipayment.dto.response.ReservationListResDto;
import com.allback.cygipayment.dto.response.ReservationResDto;
import com.allback.cygipayment.service.ReservationService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "server-admin", description = "관리자 서버와 통신하는 API")
@RestController
@RequestMapping("/server-admin")
@RequiredArgsConstructor
public class AdminResponseController {

  private final ReservationService reservationService;

  @GetMapping("/reservations")
  ResponseEntity<ReservationListResAllDto> getReservations(
      @Schema(hidden = true)
      Pageable pageable,
      @Schema(description = "페이지 별 개수", example = "10")
      @RequestParam
      int size,
      @Schema(description = "페이지", example = "0")
      @RequestParam
      int page
  ) {
    System.out.println("reservations 진입");
    ReservationListResAllDto resPage = reservationService.getAllReservations(pageable);
    return ResponseEntity.status(HttpStatus.OK).body(resPage);
  }
}
