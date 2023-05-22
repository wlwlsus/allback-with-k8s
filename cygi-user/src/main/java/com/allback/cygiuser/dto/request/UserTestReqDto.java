package com.allback.cygiuser.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

public class UserTestReqDto {
    @Getter
    @Setter
    public static class Logout {
        @NotEmpty(message = "잘못된 요청입니다.")
        @Schema(description = "액세스 토큰", example = "token")
        private String accessToken;

        @NotEmpty(message = "잘못된 요청입니다.")
        @Schema(description = "리프레쉬 토큰", example = "token")
        private String refreshToken;
    }
}
