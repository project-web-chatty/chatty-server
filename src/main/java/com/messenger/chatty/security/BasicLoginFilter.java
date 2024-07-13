package com.messenger.chatty.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.chatty.dto.request.LoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Iterator;


public class BasicLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public BasicLoginFilter(AuthenticationManager authenticationManager,TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginRequestDto loginRequestDto = obtainLoginRequestDtoFromRequest(request);
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        UsernamePasswordAuthenticationToken authToken
                = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        System.out.println("login success");

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();


        String token = tokenService.generateAccessToken(username, role);

        response.addHeader("Authorization", "Bearer " + token);
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        System.out.println("login fail");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private LoginRequestDto obtainLoginRequestDtoFromRequest(HttpServletRequest request) {
        try{
            return new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
        }
        catch (IOException e){
            // 커스텀하기
         throw new InvalidParameterException("login request is invalid");
        }

    }
}
