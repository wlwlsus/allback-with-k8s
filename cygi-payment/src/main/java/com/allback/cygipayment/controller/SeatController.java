package com.allback.cygipayment.controller;

import com.allback.cygipayment.dto.request.ReservationReqDto;
import com.allback.cygipayment.dto.request.SeatStatusChangeReqDto;
import com.allback.cygipayment.service.SeatService;
import com.allback.cygipayment.service.SeatServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "seat", description = "공연서버를 위한 API")
@Slf4j
@RestController
@RequestMapping("/server-concert")
@RequiredArgsConstructor
public class SeatController {

    private final SeatServiceImpl seatService;

    @PostMapping("/seat")
    ResponseEntity<Long> chageStatus(@RequestBody ReservationReqDto reservationReqDto) {
        Long reservationId = seatService.insertReservation(reservationReqDto);
        return new ResponseEntity<>(reservationId, HttpStatus.CREATED);
    }

    @GetMapping("/seat/{concertId}")
    ResponseEntity<List<String>> getSoldSeatInfo(@PathVariable long concertId) {
        List<String> seatList = seatService.getSoldSeatList(concertId);
        return new ResponseEntity<>(seatList, HttpStatus.OK);
    }


    @DeleteMapping("/seat/{reservationId}")
    ResponseEntity<Void> deleteReservationById(@PathVariable long reservationId) {
        seatService.deleteReservationById(reservationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/rest/{concertId}")
    ResponseEntity<Integer> getSoldSeatCnt(@PathVariable long concertId) {
        int sold = seatService.getSoldSeatCnt(concertId);
        return new ResponseEntity<>(sold, HttpStatus.OK);
    }
    @PostMapping("/rest")
    ResponseEntity<List<Integer>> getSoldSeatCnt(@RequestBody List<Long> concertIdList) {
        List<Integer> soldList = seatService.getSoldSeatCntList(concertIdList);
        return new ResponseEntity<>(soldList, HttpStatus.OK);
    }


}
