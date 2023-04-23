package com.allback.cygiuser.config.security;

import com.allback.cygiuser.config.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.allback.cygiuser.config.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.allback.cygiuser.config.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/**").permitAll();



        httpSecurity.csrf().disable()
                .authorizeRequests()
                .anyRequest().permitAll()

//                .and()
//                .logout()
//                .logoutSuccessHandler("http://localhost:3000")
                .and()
                .oauth2Login()
                .permitAll()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
//                .and()
//                .successHandler(oAuth2AuthenticationSuccessHandler);
//                .failureHandler(OAuth2AuthenticationFailureHandler);









        return httpSecurity.build();



    }

}
