package com.allback.cygiuser.config.oauth.handler;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    public OAuth2AuthenticationFailureHandler(){
        System.out.println("로그인에 실패함");
    }
}
