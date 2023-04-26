package com.allback.cygiuser.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserTestResDto {
    private String name;
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private long refreshTokenExpirationTime;
}
