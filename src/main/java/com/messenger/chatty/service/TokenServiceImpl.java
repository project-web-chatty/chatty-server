package com.messenger.chatty.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.messenger.chatty.dto.response.member.TokenResponseDto;
import com.messenger.chatty.entity.RefreshToken;
import com.messenger.chatty.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
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


  @Override
  public TokenResponseDto generateTokenPair(String username, String role){
      return TokenResponseDto.builder()
              .access_token(generateAccessToken(username, role))
              .refresh_token(generateRefreshToken(username, role))
              .build();
  }


  @Override
  public TokenResponseDto reissueToken(HttpServletRequest request) {
      String token = getTokenFromRequest(request);
      DecodedJWT decodedJWT = verifyAndDecodeToken(token, "refresh");

      String username = decodedJWT.getSubject();
      String role = decodedJWT.getClaim("role").asString();
      TokenResponseDto tokenPair = generateTokenPair(username, role);
      deleteRefreshToken(token);
      saveRefreshToken(tokenPair.getRefresh_token(), username);

      return tokenPair;
  }


    @Override
    public void logout(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        verifyAndDecodeToken(token, "refresh");
        deleteRefreshToken(token);
    }


    @Override
    public void saveRefreshToken(String token,String username){
        RefreshToken refreshToken = RefreshToken.createTokenEntity(token, username);
        tokenRepository.save(refreshToken);
    }



    public DecodedJWT verifyAndDecodeToken(String token, String tokenType){
        DecodedJWT decodedJWT;
        if (token == null) throw new IllegalStateException("토큰이 헤더에 없습니다.");
        try {
            decodedJWT = this.verifyJWT(token);
        } catch (TokenExpiredException e) {
            throw new IllegalStateException("토큰이 만료되었습니다.");
        } catch (JWTVerificationException e) {
            throw new IllegalStateException("토큰이 유효하지 않습니다.");
        }

        String category = decodedJWT.getClaim("category").asString();

        if (!category.equals(tokenType)) throw new IllegalStateException("토큰 타입이 유효하지 않습니다.");
        if(tokenType.equals("refresh") && !tokenRepository.existsByToken(token)) throw new IllegalStateException("갱신되기 이전 토큰입니다.");

        return decodedJWT;

    }



    public String getTokenFromRequest(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        return authorization.split(" ")[1];
    }

    private DecodedJWT verifyJWT(String token)  {
        Algorithm algorithm = Algorithm.HMAC256(jwtKey.getBytes(StandardCharsets.UTF_8));
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }


    private String generateAccessToken(String username,String role) {
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


    private String generateRefreshToken(String username, String role) {
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


    private void deleteRefreshToken(String token){
        tokenRepository.deleteByToken(token);
    }

}
