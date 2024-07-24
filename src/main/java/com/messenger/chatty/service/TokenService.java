package com.messenger.chatty.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.messenger.chatty.dto.response.auth.TokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface TokenService {

    TokenResponseDto generateTokenPair(String username, String role);

    void saveRefreshToken(String token,String username);

    TokenResponseDto reissueToken(HttpServletRequest request);
    void logout(HttpServletRequest request);

    String getTokenFromRequest(HttpServletRequest request);
    DecodedJWT decodeToken(String token, String tokenType);
}
