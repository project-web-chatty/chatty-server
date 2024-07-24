package com.messenger.chatty.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.chatty.dto.response.auth.TokenResponseDto;
import com.messenger.chatty.presentation.ApiResponse;
import com.messenger.chatty.presentation.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class CustomResponseSender {
    private final ObjectMapper objectMapper;

    public void sendError(HttpServletRequest request, HttpServletResponse response, ErrorStatus errorStatus, String errorDetail) throws IOException{

        ApiResponse<String> errorRes = ApiResponse.onFailure(errorStatus.getCode(), errorStatus.getMessage(),errorDetail );
        response.setStatus(errorStatus.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorRes));

    }

    public void sendToken(HttpServletResponse response, TokenResponseDto tokenResponseDto) throws IOException{
        ApiResponse<TokenResponseDto> responseDto = ApiResponse.ok(tokenResponseDto);
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseDto));
    }


}
