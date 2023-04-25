package com.allback.cygipayment.client;

import com.allback.cygipayment.dto.request.RefundRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author : cadqe13@gmail.com
 * date : 2023-04-25
 * description :
 */

@FeignClient(name = "user-server", url = "${user.server.url}", path = "${user.server.prefix}")
public interface UserServerClient {
	@PostMapping("/refund")
	void refund(@RequestBody RefundRequest request);
}

