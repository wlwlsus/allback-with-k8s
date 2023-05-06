package com.allback.cygipayment.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "concert-service", path = "${payment.server.prefix}")
public interface ConcertServerClient {

    @GetMapping("/endedConcert")
    ResponseEntity<List<Long>> getEndedConcert();
    @GetMapping("/receiverId/{concertId}")
    ResponseEntity<Long> getUserId(@PathVariable Long concertId);

    @GetMapping("/{concertId}/title")
    ResponseEntity<String> getConcertTitle(@PathVariable Long concertId);
}
