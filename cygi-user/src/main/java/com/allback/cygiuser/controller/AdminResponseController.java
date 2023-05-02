package com.allback.cygiuser.controller;

import com.allback.cygiuser.dto.response.ReservationResDto;
import com.allback.cygiuser.dto.response.UserResDto;
import com.allback.cygiuser.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "server-admin", description = "어드민 서버와 통신하는 API")
@RestController
@RequestMapping("/server-admin")
@RequiredArgsConstructor
public class AdminResponseController {

    private final UserServiceImpl userService;


    @Operation(summary = "회원 전체 목록 반환")
    @GetMapping("/users")
//    @ResponseBody
    public ResponseEntity<List<UserResDto>> getUsers(){

//        System.out.println("회원 전체 목록 반환 controller 진입");
        List<UserResDto> list = userService.getAllUserInfo();

        return new ResponseEntity<List<UserResDto>>(list, HttpStatus.OK);

    }

    @Operation(summary = "예매내역 전체 목록 반환")
    @GetMapping("/reservations")
//    @ResponseBody
    public ResponseEntity<List<ReservationResDto>> getReservations(){

//        System.out.println("예매내역 전체 목록 반환 controller 진입");
//        List<UserResDto> list = userService.getAllUserInfo();
        List<ReservationResDto> list = userService.getReservations();

        return new ResponseEntity<List<ReservationResDto>>(list, HttpStatus.OK);

    }

}
