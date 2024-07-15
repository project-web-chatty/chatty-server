package com.messenger.chatty.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.chatty.entity.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JWTFilter extends OncePerRequestFilter {

    private final AuthService authService;

    public JWTFilter(AuthService authService) {

        this.authService = authService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            // 토큰을 그저 포함하지 않은 경우에는 필터를 계속 진행.
            System.out.println("token null");
            request.setAttribute("msg","token not included"); // 권한 필요한 요청은 엔트리 포인트에서 메시지가 보여질 것
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("authorization now");
        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];


        DecodedJWT decodedJWT;
        try{
            decodedJWT = authService.verifyJWT(token);
        }
        catch (TokenExpiredException e){
            System.out.println("token expired");
            request.setAttribute("message","토큰이 만료되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }catch (JWTVerificationException e){
            System.out.println("token expired");
            request.setAttribute("message","유효하지 않은 토큰입니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // 잘못된 토큰 vs 만료된 토큰 구분하기

        if(!decodedJWT.getClaim("category").asString().equals("access")){
            System.out.println("it is not a access token");
            request.setAttribute("msg","it is not a accessToken");
            filterChain.doFilter(request, response);
            return;
        }

        //토큰에서 username과 role 획득
        String username = decodedJWT.getSubject();
        String role = decodedJWT.getClaim("role").asString();

        //userEntity를 생성하여 값 set
        Member member = Member.builder().username(username).role(role).build();

        System.out.println("role = " + role);
        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }




}
