package com.allback.cygiuser.config.security;

import com.allback.cygiuser.config.filter.MyFilter;
import com.allback.cygiuser.config.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.allback.cygiuser.config.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.allback.cygiuser.config.oauth.service.CustomOAuth2UserService;
import jakarta.persistence.Basic;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
//    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final CorsFilter corsFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity
//                .addFilterBefore(new MyFilter(), BasicAuthenticationFilter.class) // BasicAuthenticationFilter 실행 이전에 MyFilter를 추가함
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // stateless => session을 사용하지 않음
//                .authorizeHttpRequests()
                .and()
                .addFilter(corsFilter) // 모든 요청은 이 필터를 탄다
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/**").permitAll() // 요청 권한은 나중에 처리
                .and()
                .oauth2Login()
//                .permitAll()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .failureHandler(oAuth2AuthenticationFailureHandler());







        return httpSecurity.build();
    }

    /*
     * Oauth 인증 성공 핸들러
     * */
    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
//                oAuth2AuthorizationRequestBasedOnCookieRepository(),
//                jwtTokenProvider,
//                redisTemplate
        );
    }

    /*
     * Oauth 인증 실패 핸들러
     * */
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(
//                oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }











}
