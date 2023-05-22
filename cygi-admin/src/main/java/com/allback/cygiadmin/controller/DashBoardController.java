package com.allback.cygiadmin.controller;

import com.allback.cygiadmin.dto.response.BalanceResDto;
import com.allback.cygiadmin.dto.response.ReservationListResAllDto;
import com.allback.cygiadmin.dto.response.UserResDto;
import com.allback.cygiadmin.service.DashBoardServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;


@Tag(name = "dashboard", description = "대시보드 API")
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashBoardController {

    private final DashBoardServiceImpl dashBoardService;

    @Operation(summary = "전체 회원목록 조회")
    @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<Page<UserResDto>> getUsers(@RequestParam int page, @Schema(hidden = true) @RequestHeader("Authorization") String authorization) {
//        System.out.println("회원 목록 조회 controller");
        Page<UserResDto> userPage = dashBoardService.getUsers(page, authorization);
        return new ResponseEntity<>(userPage, HttpStatus.OK);
    }
    @Operation(summary = "전체 예매내역 조회")
    @GetMapping("/reservation")
    public ResponseEntity<ReservationListResAllDto> getReservations(@RequestParam int page, @RequestParam int size, @Schema(hidden = true) @RequestHeader("Authorization") String authorization) {
//        System.out.println("예매 내역 조회 Controller");
        ReservationListResAllDto reservationResDtoPage = dashBoardService.getReservations(page, size, authorization);
        return new ResponseEntity<>(reservationResDtoPage, HttpStatus.OK);
    }
    @Operation(summary = "정산 내역 조회")
    @GetMapping("/balance")
    public ResponseEntity<Page<BalanceResDto>> getBalances(@RequestParam int page, @RequestParam int size, @Schema(hidden = true) @RequestHeader("Authorization") String authorization) {
        Page<BalanceResDto> balanceList = dashBoardService.getBalances(page-1, size, authorization);
        return new ResponseEntity<>(balanceList, HttpStatus.OK);
    }
}
