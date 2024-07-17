package com.messenger.chatty.security.oauth2;

import com.messenger.chatty.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenService tokenService;
}
