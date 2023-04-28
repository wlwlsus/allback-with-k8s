package com.allback.cygipayment.client;

import java.util.HashSet;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "concert-server", url = "${concert.server.url}", path = "${concert.server.prefix}")
public interface ConcertServerClient {
    @GetMapping("/endedConcert")
    ResponseEntity<HashSet<Long>> getEndedConcert();
}
