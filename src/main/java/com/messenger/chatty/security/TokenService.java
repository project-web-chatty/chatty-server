package com.messenger.chatty.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

  // private final TokenRepository tokenRepository;
 // private final UserService userService;
  @Value("${jwt-variables.KEY}")
  private String jwtKey ;
  @Value("${jwt-variables.ISSUER}")
  private String jwtIssuer;
  @Value("${jwt-variables.EXPIRES_ACCESS_TOKEN_MINUTE}")
  private long accessTokenExpiryDuration;
  @Value("${jwt-variables.EXPIRES_REFRESH_TOKEN_MINUTE}")
  private long refreshTokenExpiryDuration;

  /*public TokenDto generateTokenPairs(String username) {
    var user = userService.getDtoByUsername(username);
    return getTokenDto(user);
  }*/

 /* public TokenDto generateTokenPairsViaRefreshToken(String refreshTokenValue) {
    var existingRefreshToken = tokenRepository.findTokenByTokenAndValidTrue(refreshTokenValue);
    if (existingRefreshToken.isEmpty()) {
      throw new EntityNotFoundException("refresh token not found!");
    }
    verifyRefreshToken(existingRefreshToken.get());
    tokenRepository.delete(existingRefreshToken.get());
    var user = userService.getDtoByUsername(existingRefreshToken.get().getUsername());
    return getTokenDto(user);
  }*/

  /*private TokenDto getTokenDto(UserDto user) {
    var accessToken = generateAccessToken(user.getUsername());
    var refreshToken = generateRefreshToken(user.getUsername());
    return TokenDto.builder()
        .refreshToken(refreshToken)
        .accessToken(accessToken)
        .user(user)
        .build();
  }*/

  public String generateAccessToken(String username,String role) {
    return JWT.create()
        .withSubject(username).withClaim("role",role)
        .withExpiresAt(
            new Date(
                System.currentTimeMillis()
                    + Duration.ofMinutes(accessTokenExpiryDuration).toMillis()))
        .withIssuer(jwtIssuer)
        .sign(Algorithm.HMAC256(jwtKey.getBytes()));
  }

  public DecodedJWT verifyJWT(String token) throws JWTVerificationException  {
    Algorithm algorithm = Algorithm.HMAC256(jwtKey.getBytes(StandardCharsets.UTF_8));
    JWTVerifier verifier = JWT.require(algorithm).build();
    return verifier.verify(token);

  }

 /* public String generateRefreshToken(String username) {
    Instant expirationTime = Instant.now().plus(Duration.ofMinutes(refreshTokenExpiryDuration));
    Token refreshToken = new Token();
    refreshToken.setUsername(username);
    refreshToken.setValid(true);
    refreshToken.setExpiryDate(expirationTime);
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken = tokenRepository.save(refreshToken);
    return refreshToken.getToken();
  }

  public void verifyRefreshToken(Token token) {
    if (token.getExpiryDate().isBefore(Instant.now())) {
      throw new RuntimeException("Token has expired and cannot be used!");
    }
  }*/
}
