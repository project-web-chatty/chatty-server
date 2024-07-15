package com.messenger.chatty.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public JWTFilter(TokenService tokenService) {

        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Authorization 헤더의 엑세스 토큰 검증
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            request.setAttribute("message","헤더에 토큰이 없습니다.");
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorization.split(" ")[1];
        DecodedJWT decodedJWT;
        try{
            decodedJWT = tokenService.verifyJWT(token);
        }
        catch (TokenExpiredException e){
            request.setAttribute("message","토큰이 만료되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }
        catch (JWTVerificationException e){
            request.setAttribute("message","유효하지 않은 토큰입니다.");
            filterChain.doFilter(request, response);
            return;
        }

        if(!decodedJWT.getClaim("category").asString().equals("access")){
            request.setAttribute("message","엑세스토큰이 아닙니다.");
            filterChain.doFilter(request, response);
            return;
        }

        String username = decodedJWT.getSubject();
        String role = decodedJWT.getClaim("role").asString();
        Member member = Member.builder().username(username).role(role).build();
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }




}
