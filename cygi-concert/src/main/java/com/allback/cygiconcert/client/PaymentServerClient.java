package com.allback.cygiconcert.client;

import com.allback.cygiconcert.dto.request.SeatStatusChangeReqDto;
import com.allback.cygiconcert.dto.response.SeatInfoResDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-server", url = "${payment.server.url}", path = "${payment.server.prefix}")
@Component
public interface PaymentServerClient {

    @GetMapping("/stage/{stageId}")
    boolean checkStageId(@PathVariable Long stageId);
    @PostMapping("/seat")
    Long chageStatus(@RequestBody SeatStatusChangeReqDto seatStatusChangeReqDto);
    @DeleteMapping("/seat/{reservationId}")
    void deleteReservationById(@PathVariable Long reservationId);

    @GetMapping("/rest/{concertId}")
    int getRestSeatCnt(Long concertId);
    @GetMapping("/seat/{concertId}")
    List<String> getSeatInfo(long concertId);
}
