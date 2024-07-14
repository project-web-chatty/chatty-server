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
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
public class WorkspaceRoleFilter extends OncePerRequestFilter {
    private final AuthService authService;
    private final PathPatternParser patternParser;
    private final WorkspaceJoinRepository workspaceJoinRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String requestUri = request.getRequestURI();
        if (requestUri.startsWith("/api/workspace/join")) {
            filterChain.doFilter(request, response);
            return;
        }

        PathPattern pattern = patternParser.parse("/api/workspace/{workspaceId}/**");
        // URI가 /api/workspace/로 시작하지 않으면 필터 체인을 진행
        PathPattern.PathMatchInfo pathMatchInfo = pattern.matchAndExtract(PathContainer.parsePath(requestUri));
        if (pathMatchInfo == null) {
            System.out.println("invalid uri in this filter");
            doFilter(request,response,filterChain);
            return;
        }

        // 유저네임 앞단의 필터에서 설정한 principal 에서 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            doFilter(request,response,filterChain);
            return;

        }
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();



        // URI에서 추출된 변수들을 가져올 수 있음
        Map<String, String> uriVariables = pathMatchInfo.getUriVariables();
        String workspaceIdStr = uriVariables.get("workspaceId");
        // workspaceId를 Long으로 변환하거나 추가적인 작업 수행 가능
        long workspaceId;
        try {
            // workspaceId를 long으로 변환
            workspaceId = Long.parseLong(workspaceIdStr);
            System.out.println("Extracted workspaceId: " + workspaceId);
        } catch (NumberFormatException e) {
            System.out.println("Failed to parse workspaceId: " + workspaceIdStr);
            return;
        }
        System.out.println("Extracted workspaceId: " + workspaceId);


        // WorkspaceMemberRepository를 사용하여 Role 조회
        WorkspaceJoin workspaceJoin = workspaceJoinRepository.findByWorkspaceIdAndMemberUsername(workspaceId, username);
        String workspaceRole = workspaceJoin.getRole();
        System.out.println("role in workspace = " + workspaceRole);

        // 기존의 role을 우선 받아온다.
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String serviceRole = auth.getAuthority();


        List<GrantedAuthority> newAuthorities = new ArrayList<>();
        newAuthorities.add(new SimpleGrantedAuthority(serviceRole));
        newAuthorities.add(new SimpleGrantedAuthority(workspaceRole));

      // 권한을 포함하는 새로운 Authentication 객체 생성
        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                authentication.getPrincipal(), null, newAuthorities);

        // SecurityContextHolder에 새로운 Authentication 설정
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);


        doFilter(request,response,filterChain);

    }


}
