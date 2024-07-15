package com.messenger.chatty.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.chatty.exception.ErrorDetail;
import com.messenger.chatty.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;



// 401
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {
        response.setContentType("application/json");
        String customMessage = (String) request.getAttribute("message");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response
                .getWriter()
                .write(objectMapper.writeValueAsString(ErrorResponse
                                .from(request.getRequestURI(), HttpStatus.UNAUTHORIZED, ErrorDetail.UN_AUTHORIZED,customMessage))
                        );
    }
}
