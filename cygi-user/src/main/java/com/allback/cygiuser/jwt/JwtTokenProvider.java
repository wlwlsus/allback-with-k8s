package com.allback.cygiuser.jwt;


import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth"; // 토큰에서 사용되는 권한 key
    private static final String BEARER_TYPE = "Bearer"; // access token type
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000L; // access token 만료 시간 (1일)
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 90 * 24 * 60 * 60 * 1000L; // refresh token 만료 시간 (90일)

    private final Key key;

//    생성자를 통해 키를 할당
    public JwtTokenProvider(@Value("${jwt.secret}")String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // secretKey를 base64로 decode하여 byte 배열로 저장
        this.key = Keys.hmacShaKeyFor(keyBytes); // byte 배열로 key 생성
    }

    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public UserTestResDto.TokenInfo generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return UserTestResDto.TokenInfo.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }




}
