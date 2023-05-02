package com.allback.cygipayment.client;

import com.allback.cygipayment.dto.request.AmountReqDto;
import java.util.List;
import javax.sound.midi.Receiver;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-server", url = "${user.server.url}", path = "${user.server.prefix}")
public interface UserServerClient {
	@PutMapping("/amount")
	void amount(@RequestBody AmountReqDto request);

	@PutMapping("/deduct/{userId}")
	void deductUserCash(@PathVariable(value = "userId") long userId, @RequestParam(name = "price") int price);

	@PostMapping("/balace")
	ResponseEntity<Void> getEndedConcert();
	@PostMapping("/point")
	ResponseEntity<Void> sendPoint(@RequestParam Long receiverId, @RequestParam Long point);
}

