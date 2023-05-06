package com.allback.cygiadmin.controller;

import com.allback.cygiadmin.dto.response.TokenResDto;
import com.allback.cygiadmin.sevice.AdminService;
import com.allback.cygiadmin.sevice.AdminServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "admin", description = "관리자 API")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminServiceImpl adminService;
    @Operation(summary = "관리자 로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenResDto> getLoginToken(@RequestBody Long userId) {
        TokenResDto tokenResDto = adminService.getLoginToken(userId);
        return new ResponseEntity<>(tokenResDto, HttpStatus.OK);
    }
}
