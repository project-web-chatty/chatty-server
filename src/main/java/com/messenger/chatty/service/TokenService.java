package com.messenger.chatty.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.messenger.chatty.dto.response.member.TokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface TokenService {

    TokenResponseDto generateTokenPair(String username, String role);

    void saveRefreshToken(String token,String username);

    TokenResponseDto reissueToken(HttpServletRequest request);
    void logout(HttpServletRequest request);

    String getTokenFromRequest(HttpServletRequest request);
    DecodedJWT verifyAndDecodeToken(String token, String tokenType);
}
