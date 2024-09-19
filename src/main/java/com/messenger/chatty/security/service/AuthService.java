package com.messenger.chatty.security.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.messenger.chatty.domain.member.dto.request.LoginRequestDto;
import com.messenger.chatty.domain.member.dto.request.PasswordUpdateRequestDto;
import com.messenger.chatty.domain.refresh.dto.response.TokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    TokenResponseDto generateTokenPair(String username, String role);

    void saveRefreshToken(String token,String username);

    TokenResponseDto reissueToken(HttpServletRequest request);

    TokenResponseDto login(LoginRequestDto loginRequestDto);

    void logout(HttpServletRequest request);

    String getTokenFromRequest(HttpServletRequest request);
    DecodedJWT decodeToken(String token, String tokenType);

    void changePassword(String username, PasswordUpdateRequestDto requestDto);
};