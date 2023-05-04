package com.allback.cygiuser.controller;


import com.allback.cygiuser.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "user-contoller", description = "유저 컨트롤러")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "포인트 조회")
    @GetMapping("/point")
    ResponseEntity<Integer> getPoint(@RequestParam("id") int userId){
        int point = userService.getPoint(userId);

        return new ResponseEntity<>(point, HttpStatus.OK);
    }
}
