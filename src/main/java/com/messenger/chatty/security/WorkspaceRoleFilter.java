package com.messenger.chatty.security;

import com.messenger.chatty.entity.WorkspaceJoin;
import com.messenger.chatty.repository.WorkspaceJoinRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.PathContainer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@RequiredArgsConstructor
public class WorkspaceRoleFilter extends OncePerRequestFilter {
    private final AuthService authService;
    private final PathPatternParser patternParser;
    private final WorkspaceJoinRepository workspaceJoinRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        PathPattern pattern = patternParser.parse("/api/workspace/{workspaceId}/**");
        // URI가 /api/workspace/로 시작하지 않으면 필터 체인을 진행
        PathPattern.PathMatchInfo pathMatchInfo = pattern.matchAndExtract(PathContainer.parsePath(requestUri));
        if (pathMatchInfo == null) {
            System.out.println("invalid uri in this filter");
            doFilter(request,response,filterChain);
            return;
        }
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        // 유저네임 앞단의 필터에서 설정한 principal 에서 가져오기
        String username = customUserDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (username != null) {
            // WorkspaceMemberRepository를 사용하여 Role 조회
            WorkspaceJoin workspaceJoin = workspaceJoinRepository.findByWorkspaceIdAndMemberUsername(workspaceId, username);


            // role을 authorities에 추가
            // 예를 들어, SecurityContextHolder를 사용하여 현재 사용자의 authorities에 role을 추가할 수 있음
        }


        System.out.println("Extracted workspaceId: " + workspaceId);
        doFilter(request,response,filterChain);

    }


    private Long extractWorkspaceId(String requestURI) {
        // "/api/workspace/{workspaceId}" 또는 "/api/workspace/{workspaceId}/**" 형태에서 workspaceId 추출
        String[] pathSegments = requestURI.split("/");
        for (int i = 0; i < pathSegments.length; i++) {
            if ("workspace".equals(pathSegments[i]) && i + 1 < pathSegments.length) {
                try {
                    return Long.parseLong(pathSegments[i + 1]);
                } catch (NumberFormatException e) {
                    // workspaceId가 올바르지 않은 경우 처리
                    return null; // 또는 예외 처리
                }
            }
        }
        return null; // workspaceId를 찾지 못한 경우
    }
}
