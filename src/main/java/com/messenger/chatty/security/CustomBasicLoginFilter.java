package com.messenger.chatty.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.chatty.dto.request.LoginRequestDto;
import com.messenger.chatty.dto.response.auth.TokenResponseDto;
import com.messenger.chatty.presentation.ErrorStatus;
import com.messenger.chatty.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final TokenService tokenService;
    private final CustomResponseSender responseSender;

    public CustomBasicLoginFilter(AuthenticationManager authenticationManager, TokenService tokenService, CustomResponseSender responseSender) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.responseSender = responseSender;
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

        TokenResponseDto tokenResponseDto = tokenService.generateTokenPair(username, role);
        tokenService.saveRefreshToken(tokenResponseDto.getRefresh_token(),username);

        responseSender.sendToken(response,tokenResponseDto);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        responseSender.sendError(request,response, ErrorStatus._BAD_REQUEST, failed.getMessage());
    }

    private LoginRequestDto obtainLoginRequestDtoFromRequest(HttpServletRequest request) {
        try{
            return new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
        }
        catch (IOException e){
            return null;
        }
    }


}
