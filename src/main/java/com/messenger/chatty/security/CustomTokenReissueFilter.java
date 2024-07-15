package com.messenger.chatty.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.chatty.exception.ErrorDetail;
import com.messenger.chatty.exception.ErrorResponse;
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
    private final AuthService authService;
    private final ObjectMapper objectMapper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if ("/api/auth/reissue".equals(requestURI)) {

            try{
                System.out.println("CustomTokenReissueFilter is invoked for URL: " + requestURI);
                authService.reIssueRefreshToken(request,response);
            }
            catch (Exception e){
                sendError(request,response,401,e.getMessage());
                return;
            }
        }
        doFilter(request, response,filterChain);

    }
    private void sendError(HttpServletRequest request, HttpServletResponse response, int status, String errorMessage) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);
        ErrorResponse errorResponse = ErrorResponse.from(request.getRequestURI(), HttpStatus.UNAUTHORIZED, ErrorDetail.UN_AUTHORIZED,errorMessage);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

}
