package com.allback.cygiuser.dto.response;

import lombok.*;

public class UserTestResDto {

  @Builder
  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UserInfo {

    private TokenInfo token;

    private LoginUserRes user;

  }

  @Builder
  @Getter
  @Setter
  @AllArgsConstructor
  public static class TokenInfo {

    private String grantType;

    private String accessToken;

    private String refreshToken;

    private Long refreshTokenExpirationTime;

  }

  @Builder
  @Getter
  public static class LoginUserRes {


    private String email;

    private String nickname;

    private int theme;

    private String profile;

  }


}
