package com.allback.cygiadmin.client;

import com.allback.cygiadmin.dto.response.ReservationResDto;
import com.allback.cygiadmin.dto.response.UserResDto;
import io.lettuce.core.StreamMessage;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment-server", url = "${payment.server.url}", path = "${payment.server.prefix}")
@Component
public interface PaymentServerClient {
    @GetMapping("/reservations")
    ResponseEntity<Page<ReservationResDto>> getReservations(@RequestParam("page") int page);
}
