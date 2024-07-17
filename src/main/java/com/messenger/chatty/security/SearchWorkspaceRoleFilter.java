package com.messenger.chatty.security;

import com.messenger.chatty.entity.WorkspaceJoin;
import com.messenger.chatty.repository.WorkspaceJoinRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.PathContainer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
public class SearchWorkspaceRoleFilter extends OncePerRequestFilter {
    private final PathPatternParser patternParser;
    private final WorkspaceJoinRepository workspaceJoinRepository;
    @Override
    @Transactional(readOnly = true)
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //URI 패턴 확인
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/workspace/join")) {
            doFilter(request,response,filterChain);
            return;
        }
        PathPattern.PathMatchInfo pathMatchInfo = patternParser.parse("/api/workspace/{workspaceId}/**")
                .matchAndExtract(PathContainer.parsePath(requestURI));
        if (pathMatchInfo == null) {
            doFilter(request,response,filterChain);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            doFilter(request,response,filterChain);
            return;

        }
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String serviceRole = auth.getAuthority();

        //인증된 유저가 ADMIN 인 경우 DB 조회 없이 다음 필터를 진행
        if(serviceRole.equals("ROLE_ADMIN")) {
            doFilter(request,response,filterChain);
            return;
        }

        Map<String, String> uriVariables = pathMatchInfo.getUriVariables();
        String variable = uriVariables.get("workspaceId");
        long workspaceId;
        try {
            workspaceId = Long.parseLong(variable);
        } catch (NumberFormatException e) {
            doFilter(request,response,filterChain);
            return;
        }

        Optional<WorkspaceJoin> workspaceJoinOptional = workspaceJoinRepository.findByWorkspaceIdAndMemberUsername(workspaceId, username);
        if(workspaceJoinOptional.isEmpty()){
            doFilter(request,response,filterChain);
            return;
        }

        WorkspaceJoin workspaceJoin = workspaceJoinOptional.get();
        String workspaceRole = workspaceJoin.getRole();
        // immutable 객체 authorities 를 새로 생성
        List<GrantedAuthority> newAuthorities = new ArrayList<>();
        newAuthorities.add(new SimpleGrantedAuthority(serviceRole));
        newAuthorities.add(new SimpleGrantedAuthority(workspaceRole));

        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                authentication.getPrincipal(), null, newAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
        doFilter(request,response,filterChain);
    }

}
