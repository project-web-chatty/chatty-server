package com.messenger.chatty.security.service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.messenger.chatty.domain.member.dto.request.LoginRequestDto;
import com.messenger.chatty.domain.member.dto.request.PasswordUpdateRequestDto;
import com.messenger.chatty.domain.refresh.dto.response.TokenResponseDto;
import com.messenger.chatty.domain.member.entity.Member;
import com.messenger.chatty.domain.refresh.entity.RefreshToken;
import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.exception.custom.AuthException;
import com.messenger.chatty.domain.member.repository.MemberRepository;
import com.messenger.chatty.domain.refresh.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final MemberRepository memberRepository;
  private final TokenRepository tokenRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
              .accessToken(generateAccessToken(username, role))
              .refreshToken(generateRefreshToken(username, role))
              .build();
  }


  @Override
  public TokenResponseDto reissueToken(HttpServletRequest request) {
      String token = getTokenFromRequest(request);
      DecodedJWT decodedJWT = decodeToken(token, "refresh");

      String username = decodedJWT.getSubject();
      String role = decodedJWT.getClaim("role").asString();

      RefreshToken tokenEntity = tokenRepository.findByUsername(username);
      if(tokenEntity == null )
          throw new AuthException(ErrorStatus.AUTH_INVALID_TOKEN);
      if(!tokenEntity.getToken().equals(token))
          throw new AuthException(ErrorStatus.AUTH_OUTDATED_REFRESH_TOKEN);

      TokenResponseDto tokenPair = generateTokenPair(username, role);
      saveRefreshToken(tokenPair.getRefreshToken(), username);
      return tokenPair;
  }

    @Override
    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new AuthException(ErrorStatus.AUTH_FAIL_LOGIN));
        String password = member.getPassword();
        if(!bCryptPasswordEncoder.matches(loginRequestDto.getPassword(),password))
            throw new AuthException(ErrorStatus.AUTH_FAIL_LOGIN);

        String username = member.getUsername();
        String serviceRole = member.getRole();

        TokenResponseDto tokenResponseDto = this.generateTokenPair(username, serviceRole);
        this.saveRefreshToken(tokenResponseDto.getRefreshToken(),username);

        return tokenResponseDto;
    }


    @Override
    public void logout(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        DecodedJWT decodedJWT = decodeToken(token, "access");
        String username = decodedJWT.getSubject();

        // 일단 유저 한명당 디바이스에 관계없이 하나의 리프레시 토큰 만을 가질 수 있도록 함
        tokenRepository.deleteByUsername(username);
    }


    @Override
    public void saveRefreshToken(String token,String username){
        RefreshToken refreshToken = RefreshToken.createTokenEntity(token, username);

        // 존재한다면 삭제
        tokenRepository.deleteByUsername(username);

        // 쿼리 순서 보장
        tokenRepository.flush();

        tokenRepository.save(refreshToken);
    }


    public String getTokenFromRequest(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        return authorization.split(" ")[1];
    }

    public DecodedJWT decodeToken(String token, String tokenType){
        DecodedJWT decodedJWT;
        if (token == null) throw new AuthException(ErrorStatus.AUTH_NULL_TOKEN);
        try {
            decodedJWT = this.verifyJWT(token);
        } catch (TokenExpiredException e) {
            throw new AuthException(ErrorStatus.AUTH_EXPIRED_TOKEN);
        } catch (JWTVerificationException e) {
            throw new AuthException(ErrorStatus.AUTH_INVALID_TOKEN);
        }
        String category = decodedJWT.getClaim("category").asString();
        if (!category.equals(tokenType)) throw new AuthException(ErrorStatus.AUTH_TYPE_MISMATCH_TOKEN);
        return decodedJWT;

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

    @Override
    public void changePassword(String username, PasswordUpdateRequestDto requestDto) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException(ErrorStatus.AUTH_FAIL_LOGIN));

        String password = member.getPassword();
        if(!bCryptPasswordEncoder.matches(requestDto.getOldPassword(),password))
            throw new AuthException(ErrorStatus.AUTH_FAIL_PASSWORD_MATCHING);

        member.changePassword(bCryptPasswordEncoder.encode(requestDto.getNewPassword()));
        tokenRepository.deleteByUsername(username);
    }
}
