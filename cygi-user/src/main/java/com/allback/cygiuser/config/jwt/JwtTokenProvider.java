package com.allback.cygiuser.config.jwt;


import com.allback.cygiuser.dto.response.UserTestResDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Slf4j
@Component
public class JwtTokenProvider {

  private static final String AUTHORITIES_KEY = "auth";
  private static final String BEARER_TYPE = "Bearer";
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000L;          // 1일
  private static final long REFRESH_TOKEN_EXPIRE_TIME = 90 * 24 * 60 * 60 * 1000L;    // 90일
  private static final int REFRESH_TOKEN_EXPIRE_TIME_COOKIE = Integer.MAX_VALUE;

  private final Key key;

  public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public UserTestResDto.TokenInfo generateToken(String id, String role) {
    long now = (new Date()).getTime();
    // Access Token 생성
    String accessToken = Jwts.builder()
        .setSubject(id)
        .claim(AUTHORITIES_KEY, role)
        .signWith(key, SignatureAlgorithm.HS256)
        .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))
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

  // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
  public Authentication getAuthentication(String accessToken) {
    // 토큰 복호화
    Claims claims = parseClaims(accessToken);

    if (claims.get(AUTHORITIES_KEY) == null) {
      throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    }

    // 클레임에서 권한 정보 가져오기
    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    // UserDetails 객체를 만들어서 Authentication 리턴
    UserDetails principal = new User(claims.getSubject(), "", authorities);
    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
  }

  // 토큰 정보를 검증하는 메서드
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT Token", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty.", e);
    }
    return false;
  }

  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }

  public Long getExpiration(String accessToken) {
    // accessToken 남은 유효시간
    Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
    // 현재 시간
    long now = new Date().getTime();
    return (expiration.getTime() - now);
  }

  public static int getRefreshTokenExpireTimeCookie() {
    return REFRESH_TOKEN_EXPIRE_TIME_COOKIE;
  }
}
