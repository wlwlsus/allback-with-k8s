package com.allback.cygibatch.client;

import com.allback.cygibatch.dto.request.AmountReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-server", url = "${user.server.url}", path = "${user.server.prefix}")
public interface UserServerClient {

	@PutMapping("/point")
	ResponseEntity<Void> sendPoint(@RequestParam long receiverId, @RequestParam long point);
}

