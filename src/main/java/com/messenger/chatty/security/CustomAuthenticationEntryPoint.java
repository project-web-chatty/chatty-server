package com.messenger.chatty.security;

import com.messenger.chatty.presentation.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;



// 401
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final CustomResponseSender responseSender;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {
        Object errorStatus = request.getAttribute("errorStatus");
        if(errorStatus instanceof ErrorStatus){
            responseSender.sendError(request,response, (ErrorStatus) errorStatus, authException.getMessage());
            return;
        }
        responseSender.sendError(request,response, ErrorStatus._INTERNAL_SERVER_ERROR, authException.getMessage());

    }
}
