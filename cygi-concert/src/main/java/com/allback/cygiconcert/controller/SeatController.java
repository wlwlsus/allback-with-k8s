package com.allback.cygiconcert.controller;

import com.allback.cygiconcert.dto.request.SeatStatusChangeReqDto;
import com.allback.cygiconcert.dto.response.SeatRestCntResDto;
import com.allback.cygiconcert.dto.response.SeatInfoResDto;
import com.allback.cygiconcert.service.SeatServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "seat", description = "좌석 API")
@RestController
@RequestMapping("/seat")
@RequiredArgsConstructor
public class SeatController {

    private final SeatServiceImpl seatService;


    @Operation(summary = "좌석 상태 변경")
    @PostMapping("")
    public ResponseEntity<Long> changeSeatStatus(
        @RequestBody SeatStatusChangeReqDto seatStatusChangeReqDto) throws Exception {
        Long reservationId = seatService.changeStatus(seatStatusChangeReqDto);
        return new ResponseEntity<>(reservationId, HttpStatus.CREATED);
    }

    @Operation(summary = "진행중인 예약 삭제")
    @DeleteMapping("/delete/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable long reservationId) {
        seatService.deleteReservationById(reservationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "남은 좌석 조회")
    @GetMapping("/rest/{concertId}")
    public ResponseEntity<SeatRestCntResDto> getSeatRestCnt(@PathVariable long concertId)
        throws Exception {
        SeatRestCntResDto seatRestCntResDto = seatService.getRestSeatCnt(concertId);
        return new ResponseEntity<>(seatRestCntResDto, HttpStatus.OK);
    }

    @Operation(summary = "좌석 정보 조회")
    @GetMapping("/{concertId}")
    public ResponseEntity<Map<String, Object>> getSeatInfo(@PathVariable long concertId)
        throws Exception {
        Map<String, Object> seatStatus = seatService.getStatus(concertId);
        return new ResponseEntity<>(seatStatus, HttpStatus.OK);
    }


}
