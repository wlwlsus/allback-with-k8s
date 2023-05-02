package com.allback.cygiconcert.controller;

import com.allback.cygiconcert.service.ConcertServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "concert - payment", description = "payment와 통신하는 API")
@RestController
@RequestMapping("/server-payment")
@RequiredArgsConstructor
public class PaymentServerController {
    private final ConcertServiceImpl concertService;
    @Operation(summary = "금일 종료된 공연 조회")
    @GetMapping("/endedConcert")
    public ResponseEntity<List<Long>> getEndedConcert() {
        List<Long> endedConcert = concertService.getEndedConcert(LocalDate.now());
        return new ResponseEntity<>(endedConcert, HttpStatus.OK);
    }
    @Operation(summary = "주최자 ID 조회")
    @GetMapping("/receiverId/{concertId}")
    ResponseEntity<Long> getUserId(@PathVariable long concertId){
        Long userId = concertService.getUserId(concertId);
        return new ResponseEntity<>(userId, HttpStatus.OK);
    };
}
