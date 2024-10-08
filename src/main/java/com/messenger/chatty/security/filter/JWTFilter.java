package com.messenger.chatty.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.messenger.chatty.domain.member.entity.Member;
import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.exception.GeneralException;
import com.messenger.chatty.security.dto.CustomUserDetails;
import com.messenger.chatty.security.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final AuthService authService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token;
        DecodedJWT decodedJWT;
        try{
            token = authService.getTokenFromRequest(request);
            decodedJWT = authService.decodeToken(token,"access");
        }
        catch (GeneralException e){
            log.info("url = {}",request.getRequestURI());
            request.setAttribute("errorStatus",e.getErrorStatus());
            filterChain.doFilter(request, response);
            return;
        }
        catch (RuntimeException e){
            request.setAttribute("errorStatus", ErrorStatus._INTERNAL_SERVER_ERROR);
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
