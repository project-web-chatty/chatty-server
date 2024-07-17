package com.messenger.chatty.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.chatty.exception.ErrorDetail;
import com.messenger.chatty.exception.ErrorResponse;
import com.messenger.chatty.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomTokenReissueFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // URI 확인
        String requestURI = request.getRequestURI();
        if (!requestURI.matches("/api/auth/reissue")|| !request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 재발급
       try{
                tokenService.reIssueToken(request,response);
       }
       catch (RuntimeException e){
                sendError(request,response,e.getMessage());
       }
    }
    private void sendError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(401);
        ErrorResponse errorResponse = ErrorResponse.from(request.getRequestURI(), HttpStatus.UNAUTHORIZED, ErrorDetail.UN_AUTHORIZED,errorMessage);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

}
