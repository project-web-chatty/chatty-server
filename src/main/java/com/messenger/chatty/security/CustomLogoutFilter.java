package com.messenger.chatty.security;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.chatty.exception.ErrorDetail;
import com.messenger.chatty.exception.ErrorResponse;
import com.messenger.chatty.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;



@RequiredArgsConstructor
public class CustomLogoutFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // URI 확인
        String requestURI = request.getRequestURI();
        if (!requestURI.matches("/api/auth/logout")|| !request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 토큰 검증
        String refreshToken = tokenService.getRefreshTokenFromRequest(request);
        if (refreshToken == null) {
            sendError(request,response,"쿠키에 토큰이 없습니다.");
            return;
        }
        DecodedJWT decodedJWT ;
        try {
            decodedJWT = tokenService.verifyJWT(refreshToken);

        } catch (TokenExpiredException e) {
            sendError(request,response,"토큰이 이미 만료되었습니다.");
            return;
        }catch (JWTVerificationException e){
            sendError(request,response,"유효한 토큰이 아닙니다.");
            return;
        }
        String category =decodedJWT.getClaim("category").asString();
        if (!category.equals("refresh")) {
            sendError(request,response,"리프레시 토큰이 아닙니다.");
            return;
        }
        if (!tokenService.checkExistByToken(refreshToken)) {
            sendError(request,response,"유효한 토큰이 아닙니다.");
            return;
        }

        //로그아웃 진행
        tokenService.deleteRefreshToken(refreshToken);
        Cookie nullCookie = CookieGenerator.generateCookie("refresh", null, 0);
        response.addCookie(nullCookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
    private void sendError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(400);
        ErrorResponse errorResponse = ErrorResponse.from(request.getRequestURI(),
                HttpStatus.BAD_REQUEST, ErrorDetail.INVALID_LOGOUT_REQUEST,errorMessage);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
