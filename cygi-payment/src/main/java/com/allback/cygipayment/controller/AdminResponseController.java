package com.allback.cygipayment.controller;

import com.allback.cygipayment.dto.response.ReservationResDto;
import com.allback.cygipayment.service.ReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "server-admin", description = "어드민 서버와 통신하는 API")
@RestController
@RequestMapping("/server-admin")
@RequiredArgsConstructor
public class AdminResponseController {

    private final ReservationService reservationService;

    @GetMapping("/reservations")
    ResponseEntity<Page<ReservationResDto>> getReservations(int page){
        System.out.println("reservations 진입");
        Page<ReservationResDto> resPage = reservationService.getReservations(page - 1);
        return new ResponseEntity<Page<ReservationResDto>>(resPage, HttpStatus.OK);
    }
}
