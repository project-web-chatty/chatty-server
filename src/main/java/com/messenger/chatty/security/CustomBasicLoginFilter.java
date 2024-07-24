package com.messenger.chatty.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.chatty.dto.request.LoginRequestDto;
import com.messenger.chatty.dto.response.member.TokenResponseDto;
import com.messenger.chatty.exception.ErrorDetail;
import com.messenger.chatty.exception.ErrorResponse;
import com.messenger.chatty.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;


public class CustomBasicLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final TokenService tokenService;

    public CustomBasicLoginFilter(AuthenticationManager authenticationManager, TokenService tokenService, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginRequestDto loginRequestDto = obtainLoginRequestDtoFromRequest(request);
        if(loginRequestDto == null) throw new AuthenticationServiceException("잘못된 로그인 요청입니다.");
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        UsernamePasswordAuthenticationToken authToken
                = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String accessToken = tokenService.generateAccessToken(username, role);
        String refreshToken = tokenService.generateRefreshToken(username,role);
        tokenService.saveRefreshToken(refreshToken,username);

        sendTokens(response,refreshToken,accessToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        sendError(request,response, "로그인에 실패하였습니다.");
    }

    private LoginRequestDto obtainLoginRequestDtoFromRequest(HttpServletRequest request) {
        try{
            return new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
        }
        catch (IOException e){
            return null;
        }
    }

    private void sendError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(400);
        ErrorResponse errorResponse = ErrorResponse.from(request.getRequestURI(),
                HttpStatus.BAD_REQUEST, ErrorDetail.INVALID_LOGIN_REQUEST,errorMessage);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    private void sendTokens(HttpServletResponse response, String refreshToken, String accessToken) throws IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        TokenResponseDto loginResponse = TokenResponseDto.builder()
                .access_token(accessToken)
                .refresh_token(refreshToken)
                .build();
        response.getWriter().write(objectMapper.writeValueAsString(loginResponse));
    }

}
