package com.allback.cygiadmin.client;

import com.allback.cygiadmin.dto.response.ReservationResDto;
import com.allback.cygiadmin.dto.response.UserResDto;
import io.lettuce.core.StreamMessage;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "payment-server", url = "${user.server.url}", path = "${user.server.prefix}")
@Component
public interface PaymentServerClient {
    @GetMapping("/reservations")
    ResponseEntity<List<ReservationResDto>> getReservations();
}
