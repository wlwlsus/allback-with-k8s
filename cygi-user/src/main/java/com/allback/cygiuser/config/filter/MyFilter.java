package com.allback.cygiuser.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.http.HttpRequest;

public class MyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if(req.getMethod().equals("POST")) {
            System.out.println("POST 요청됨");
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);

            if(req.getMethod().equals("jwt")){
                chain.doFilter(req, res);
            }else{
                System.out.println("인증 안됨");
            }


        }else{
            System.out.println("POST 요청이 아님");
        }

//        System.out.println("my filter is activated"); // 로직 작동
//        chain.doFilter(req, res); // 다시 필터로 돌아가라
    }
}
