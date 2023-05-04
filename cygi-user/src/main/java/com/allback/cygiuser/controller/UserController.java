package com.allback.cygiuser.controller;


import com.allback.cygiuser.dto.request.UserTestReqDto;
import com.allback.cygiuser.service.UserService;
import com.allback.cygiuser.util.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "user-contoller", description = "유저 컨트롤러")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "포인트 조회")
    @GetMapping("/point/{userId}")
    ResponseEntity<Integer> getPoint(@PathVariable("userId") int userId){
        int point = userService.getPoint(userId);

        return new ResponseEntity<>(point, HttpStatus.OK);
    }

    @Operation(summary = "로그아웃 API", responses = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "400", description = "로그아웃 실패"),
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody @Validated UserTestReqDto.Logout logout, Errors errors) {
        if (errors.hasErrors()) {
            return Response.badRequest("로그아웃을 실패하였습니다.");
        }
        return userService.logout(logout);
    }
}
