package com.allback.cygiconcert.client;

import com.allback.cygiconcert.dto.request.ReservationReqDto;
import com.allback.cygiconcert.dto.request.SeatStatusChangeReqDto;
import com.allback.cygiconcert.dto.response.SeatInfoResDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-server", url = "${payment.server.url}", path = "${payment.server.prefix}")
@Component
public interface PaymentServerClient {

    @PostMapping("/seat")
    ResponseEntity<Long> chageStatus(@RequestBody ReservationReqDto reservationReqDto);

    @DeleteMapping("/seat/{reservationId}")
    ResponseEntity<Void> deleteReservationById(@PathVariable long reservationId);

    @GetMapping("/rest/{concertId}")
    ResponseEntity<Integer> getRestSeatCnt(@PathVariable long concertId);

    @GetMapping("/seat/{concertId}")
    ResponseEntity<List<String>> getSeatInfo(@PathVariable long concertId);
    @PostMapping("/rest")
    ResponseEntity<List<Integer>> getRestSeatCntList(@RequestBody List<Long> concertIdList);
}
