package com.messenger.chatty.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.messenger.chatty.entity.RefreshToken;
import com.messenger.chatty.repository.TokenRepository;
import com.messenger.chatty.security.CookieGenerator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{
  private final TokenRepository tokenRepository;

  @Value("${variables.jwt.KEY}")
  private String jwtKey ;
  @Value("${variables.jwt.ISSUER}")
  private String jwtIssuer;
  @Value("${variables.jwt.EXPIRES_ACCESS_TOKEN_MINUTE}")
  private long accessTokenExpiryDuration;
  @Value("${variables.jwt.EXPIRES_REFRESH_TOKEN_MINUTE}")
  private long refreshTokenExpiryDuration;

  public String generateAccessToken(String username,String role) {
    return JWT.create()
        .withSubject(username)
            .withClaim("role",role)
            .withClaim("category","access")
        .withExpiresAt(
            new Date(
                System.currentTimeMillis()
                    + Duration.ofMinutes(accessTokenExpiryDuration).toMillis()))
        .withIssuer(jwtIssuer)
        .sign(Algorithm.HMAC256(jwtKey.getBytes()));
  }


  public String generateRefreshToken(String username, String role) {
    return JWT.create()
            .withSubject(username)
            .withClaim("role", role)
            .withClaim("category","refresh")
            .withExpiresAt(
                    new Date(
                            System.currentTimeMillis()
                                    + Duration.ofMinutes(refreshTokenExpiryDuration).toMillis()))
            .withIssuer(jwtIssuer)
            .sign(Algorithm.HMAC256(jwtKey.getBytes()));
  }
  public DecodedJWT verifyJWT(String token)  {
        Algorithm algorithm = Algorithm.HMAC256(jwtKey.getBytes(StandardCharsets.UTF_8));
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
  }


  public void saveRefreshToken(String token,String username){
      RefreshToken refreshToken = RefreshToken.createTokenEntity(token, username);
      tokenRepository.save(refreshToken);
  }

  @Transactional(readOnly = true)
  public boolean checkExistByToken(String token){
      return tokenRepository.existsByToken(token);
  }
  public void deleteRefreshToken(String token){
      tokenRepository.deleteByToken(token);
  }


  public void reIssueToken(HttpServletRequest request, HttpServletResponse response) {
      String refreshToken = getRefreshTokenFromRequest(request);
      if (refreshToken == null) {
          throw new IllegalStateException("토큰이 쿠키에 없습니다.");
      }
      DecodedJWT decodedJWT;
      try {
          decodedJWT = this.verifyJWT(refreshToken);

      } catch (TokenExpiredException e) {
          throw new IllegalStateException("토큰이 만료되었습니다.");
      } catch (JWTVerificationException e) {
          throw new IllegalStateException("토큰이 유효하지 않습니다.");
      }

      String category = decodedJWT.getClaim("category").asString();

      if (!category.equals("refresh")) {
          throw new IllegalStateException("리프레시 토큰이 아닙니다.");
      }

      if (!this.checkExistByToken(refreshToken)) {
          throw new IllegalStateException("인증이 거부된 토큰입니다.");
      }


      String username = decodedJWT.getSubject();
      String role = decodedJWT.getClaim("role").asString();

      String newAccessToken = generateAccessToken(username, role);
      String newRefreshToken = generateRefreshToken(username, role);
      deleteRefreshToken(refreshToken);
      saveRefreshToken(newRefreshToken, username);

      response.addHeader("Authorization", "Bearer " + newAccessToken);
      response.addCookie(CookieGenerator.generateCookie("refresh_token", newRefreshToken, 7 * 24 * 60 * 60));
  }
    public String getRefreshTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh_token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


}
