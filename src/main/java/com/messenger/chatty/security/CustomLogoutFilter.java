package com.messenger.chatty.security;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


public class CustomLogoutFilter extends GenericFilterBean {
    private final AuthService authService;

    public CustomLogoutFilter(AuthService authService) {
        this.authService =authService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        //path and method verify
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("/api/auth/logout")|| !request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        // authController의 reissue 메서드와 동일하므로 리팩터링 필수
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh_token")) {
                refreshToken = cookie.getValue();
            }
        }
        if (refreshToken == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        DecodedJWT decodedJWT =null;
        //expired check
        try {
            decodedJWT = authService.verifyJWT(refreshToken);

        } catch (TokenExpiredException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category =decodedJWT.getClaim("category").asString();

        if (!category.equals("refresh")) {
            System.out.println("cateogory:  "+category);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // db에 저장되어 있는지 확인
        if (!authService.checkExistByToken(refreshToken)) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }



        //로그아웃 진행
        //Refresh 토큰 DB에서 제거
        authService.deleteRefreshToken(refreshToken);

        //Refresh 토큰 Cookie 값 0
        Cookie nullCookie = CookieGenerator.generateCookie("refresh", null, 0);

        response.addCookie(nullCookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
