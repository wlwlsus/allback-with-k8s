package com.allback.cygiuser.config.oauth.handler;

import com.allback.cygiuser.config.oauth.entity.ProviderType;
import com.allback.cygiuser.config.oauth.info.OAuth2UserInfo;
import com.allback.cygiuser.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.oauth2.authorizedRedirectUris}")
    private String redirectUri;

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTemplate<String, String> redisTemplate;



//    determineTargetUrl을 통해 로그인 성공시 제대로 진입하는지 확인하고 Authentication에서 OAuth2UserInfo를 제대로 만들 수 있는지 체크할것
//    체크 이후에 GPT의 가이드 라인을 따라 Redis와 JWT를 공급하고 관리하는것을 시도해보기
//    Fail handler는 기존의 코드를 많이 따라가도 될것 같다


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.onAuthenticationSuccess(request, response, authentication);
        determineTargetUrl(request, response, authentication);
//        고정 url로 redirect
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, redirectUri);
        System.out.println("로그인 성공");
    }


    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        인증을 위한 토큰 객체
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
//        role?
        ProviderType providerType = ProviderType.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());

        OidcUser user = ((OidcUser) authentication.getPrincipal());
//        공급자는 여럿이지만 필드명이 같기 때문에 팩토리 패턴을 사용하지 않겠다
//        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        System.out.println("------------------- >>>>>>>>>>>>>>>>>>>>> ------------------");
        OAuth2UserInfo userInfo = OAuth2UserInfo.builder()
                .attributes(user.getAttributes())
                .name(user.getName())
//                .nameAttributeKey(user.getNa)
                .build();



//        Collection<? extends GrantedAuthority> authorities = ((OidcUser) authentication.getPrincipal()).getAuthorities();
//
//        Authority roleType = hasAuthority(authorities, Authority.ROLE_ADMIN.name()) ? Authority.ROLE_ADMIN : Authority.ROLE_USER;
//
//        UserTestResDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(userInfo.getEmail(), roleType.name());
//
//        redisTemplate.opsForValue()
//                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        // 리다이렉트할 URL을 직접 지정합니다.
        return "https://example.com/dashboard";
    }


    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
//        인증 정보 삭제
        super.clearAuthenticationAttributes(request);
//        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }


}
