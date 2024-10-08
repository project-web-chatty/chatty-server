package com.messenger.chatty.security.filter;

import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.security.util.CustomResponseSender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final CustomResponseSender responseSender;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException {
        responseSender.sendError(request,response, ErrorStatus.AUTH_UNAUTHORIZED_ACCESS, accessDeniedException.getMessage());

    }
}
