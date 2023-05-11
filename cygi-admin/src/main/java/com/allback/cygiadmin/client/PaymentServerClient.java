package com.allback.cygiadmin.client;

import com.allback.cygiadmin.dto.response.ReservationResDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Headers("Content-Type: application/json")
@FeignClient(name = "payment-server", url = "${admin.server.payment}", path = "${admin.server.prefix}")
@Component
public interface PaymentServerClient {
    @GetMapping("/reservations")
    ResponseEntity<List<ReservationResDto>> getReservations(@RequestParam("page") int page, @RequestParam("size") int size, @RequestHeader("Authorization") String authorization);
}
