package com.allback.cygiadmin.client;

import com.allback.cygiadmin.dto.response.BalanceResDto;
import com.allback.cygiadmin.dto.response.ReservationListResAllDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment-service", url = "${payment.server.url}",path = "${payment.server.prefix}")
public interface PaymentServerClient {
    @GetMapping("/reservations")
    ResponseEntity<ReservationListResAllDto> getReservations(@RequestParam("page") int page, @RequestParam("size") int size);
    @GetMapping("/balances")
    ResponseEntity<Page<BalanceResDto>> getBalances(@RequestParam("page") int page, @RequestParam("size") int size);
}
