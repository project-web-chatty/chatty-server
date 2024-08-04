package com.messenger.chatty.security.oauth2;
import com.messenger.chatty.domain.refresh.dto.response.TokenResponseDto;
import com.messenger.chatty.security.dto.CustomUserDetails;
import com.messenger.chatty.security.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class CustomOauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final AuthService authService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, IOException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        TokenResponseDto tokenResponseDto = authService.generateTokenPair(username, role);
        authService.saveRefreshToken(tokenResponseDto.getRefresh_token(),username);

        String redirectURL = "http://localhost:3000/oauth2/success?refresh_token="
                + URLEncoder.encode(tokenResponseDto.getRefresh_token(), StandardCharsets.UTF_8)
                + "&access_token=" +  URLEncoder.encode(tokenResponseDto.getAccess_token(), StandardCharsets.UTF_8);
        response.sendRedirect(redirectURL);

    }
}
