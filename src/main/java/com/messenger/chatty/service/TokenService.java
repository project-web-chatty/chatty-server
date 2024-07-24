package com.messenger.chatty.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.messenger.chatty.dto.response.member.TokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenService {
    String generateAccessToken(String username,String role);
    String generateRefreshToken(String username, String role);
    DecodedJWT verifyJWT(String token);
    void saveRefreshToken(String token,String username);
    boolean checkExistByToken(String token);
    void deleteRefreshToken(String token);

    String getRefreshTokenFromRequest(HttpServletRequest request);


    TokenResponseDto reIssueToken(HttpServletRequest request);
    void logout(HttpServletRequest request);
}
