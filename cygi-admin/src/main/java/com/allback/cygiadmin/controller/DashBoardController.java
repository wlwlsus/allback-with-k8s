package com.allback.cygiadmin.controller;

import com.allback.cygiadmin.dto.response.BalanceResDto;
import com.allback.cygiadmin.dto.response.ReservationResDto;
import com.allback.cygiadmin.dto.response.TokenResDto;
import com.allback.cygiadmin.dto.response.UserResDto;
import com.allback.cygiadmin.sevice.DashBoardServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "dashboard", description = "대시보드 API")
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashBoardController {
    private final DashBoardServiceImpl dashBoardService;
    @Operation(summary = "전체 회원목록 조회")
    @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<List<UserResDto>> getUsers() {
//        System.out.println("회원 목록 조회 controller");
        List<UserResDto> userList = dashBoardService.getUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }
    @Operation(summary = "전체 예매내역 조회")
    @GetMapping("/reservation")
    public ResponseEntity<List<ReservationResDto>> getReservations() {
//        System.out.println("예매 내역 조회 Controller");
        List<ReservationResDto> reservationResDtoList = dashBoardService.getReservations();
        return new ResponseEntity<>(reservationResDtoList, HttpStatus.OK);
    }
    @Operation(summary = "정산 내역 조회")
    @GetMapping("/balance")
    public ResponseEntity<List<BalanceResDto>> getBalances() {
        List<BalanceResDto> balansceList = dashBoardService.getBalances();
        return new ResponseEntity<>(balansceList, HttpStatus.OK);
    }
}
