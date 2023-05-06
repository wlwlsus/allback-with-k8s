package com.allback.cygiuser.controller;

import com.allback.cygiuser.dto.response.ReservationResDto;
import com.allback.cygiuser.dto.response.UserResDto;
import com.allback.cygiuser.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "server-admin", description = "어드민 서버와 통신하는 API")
@RestController
@RequestMapping("/server-admin")
@RequiredArgsConstructor
public class AdminResponseController {

    private final UserServiceImpl userService;

    @Operation(summary = "회원 전체 목록 반환")
    @GetMapping("/users")
//    @ResponseBody
    ResponseEntity<Page<UserResDto>> getUsers(@RequestParam int page){

        System.out.println("회원 전체 목록 반환 controller 진입");
        Page<UserResDto> resPage = userService.getAllUserInfo(page - 1);

        return new ResponseEntity<Page<UserResDto>>(resPage, HttpStatus.OK);

    }
}