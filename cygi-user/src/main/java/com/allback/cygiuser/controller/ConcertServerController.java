package com.allback.cygiuser.controller;

import com.allback.cygiuser.entity.Users;
import com.allback.cygiuser.service.UserService;
import com.allback.cygiuser.util.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "user - concert", description = "콘서트 서버와 통신하는 API")
@RestController
@Slf4j
@RequestMapping("/server-concert")
@RequiredArgsConstructor
public class ConcertServerController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable long userId) {
        userService.getUser(userId);
        return Response.makeResponse(HttpStatus.OK, "유저 존재");
    }
}
