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

  @Value("${jwt-variables.KEY}")
  private String jwtKey ;
  @Value("${jwt-variables.ISSUER}")
  private String jwtIssuer;
  @Value("${jwt-variables.EXPIRES_ACCESS_TOKEN_MINUTE}")
  private long accessTokenExpiryDuration;
  @Value("${jwt-variables.EXPIRES_REFRESH_TOKEN_MINUTE}")
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
  public DecodedJWT verifyJWT(String token) throws JWTVerificationException  {
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
          // 나중에 예외 처리하기
          throw new IllegalStateException("there is no token in cookie");
      }

      DecodedJWT decodedJWT = null;
      //expired check
      try {
          decodedJWT = this.verifyJWT(refreshToken);

      } catch (TokenExpiredException e) {
          //response status code
          throw new IllegalStateException("토큰이 만료되었습니다.");
      } catch (JWTVerificationException e) {
          throw new IllegalStateException("토큰이 유효하지 않습니다.");
      }

      // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
      String category = decodedJWT.getClaim("category").asString();

      if (!category.equals("refresh")) {
          throw new IllegalStateException("리프레시 토큰이 아닙니다.");
      }

      // db에 저장되어 있는지 확인
      if (!this.checkExistByToken(refreshToken)) {
          throw new IllegalStateException("인증이 거부된 토큰입니다.");
      }


      String username = decodedJWT.getSubject();
      String role = decodedJWT.getClaim("role").asString();

      //make new JWT
      String newAccessToken = generateAccessToken(username, role);
      String newRefreshToken = generateRefreshToken(username, role);

      // 리프레시 토큰 db에 갱신 하여 변경
      deleteRefreshToken(refreshToken);
      saveRefreshToken(newRefreshToken, username);

      //response
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
