package com.allback.cygiuser.config.security;

import com.allback.cygiuser.config.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.allback.cygiuser.config.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.allback.cygiuser.config.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final CorsFilter corsFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // stateless => session을 사용하지 않음
                .and()
                .addFilter(corsFilter) // 모든 요청은 이 필터를 탄다
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/**").permitAll(); // 요청 권한은 나중에 처리



//        httpSecurity.csrf().disable()
//                .authorizeRequests()
//                .anyRequest().permitAll()

//                .and()
//                .logout()
//                .logoutSuccessHandler("http://localhost:3000")
//                .and()
        httpSecurity
                .authorizeHttpRequests()
                .and()
                .oauth2Login()
                .permitAll()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
//                .and()
//                .successHandler();
//                .failureHandler(OAuth2AuthenticationFailureHandler);







        return httpSecurity.build();



    }

}
