package com.allback.cygiadmin.client;

import com.allback.cygiadmin.dto.response.UserResDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "user-server", url = "${user.server.url}", path = "${user.server.prefix}")
@Component
public interface UserServerClient {
    @GetMapping("/users")
    ResponseEntity<Page<UserResDto>> getUsers(@RequestParam("page") int page);
}

//localhost:8000/api/v1/server-admin/users?page=0