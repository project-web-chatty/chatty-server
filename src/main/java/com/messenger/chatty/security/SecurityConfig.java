package com.messenger.chatty.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.chatty.repository.WorkspaceJoinRepository;
import com.messenger.chatty.security.oauth2.CustomOAuth2UserService;
import com.messenger.chatty.security.oauth2.CustomOauthSuccessHandler;
import com.messenger.chatty.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Collections;


@Configuration
@EnableWebSecurity // (debug = true) it should not be included in deployment ver
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenService tokenService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final WorkspaceJoinRepository workspaceJoinRepository;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomResponseSender responseSender;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // stateless session
        httpSecurity.sessionManagement((sessionManagementConfigurer ->
                sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)));

        // default setting
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(
                        handlingConfigurer -> {
                            handlingConfigurer.accessDeniedHandler(customAccessDeniedHandler);
                            handlingConfigurer.authenticationEntryPoint(customAuthenticationEntryPoint);
                        })
                .logout(AbstractHttpConfigurer::disable);

        // authorization setting
        httpSecurity
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/v3/**", "/swagger-ui/**", "/api/isHealthy",
                                "/api/member/signup","/api/member/check/username","/api/auth/reissue"
                                ,"/api/auth/logout").permitAll()
                        .requestMatchers("/api/workspace/join/**","/api/workspace").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/workspace/**").hasAnyRole("ADMIN","WORKSPACE_OWNER","WORKSPACE_MEMBER")
                        .requestMatchers("/api/workspace/**").hasAnyRole("ADMIN","WORKSPACE_OWNER")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated());

        // custom filters settings
        httpSecurity.addFilterAt(new CustomBasicLoginFilter(authenticationManager(authenticationConfiguration), tokenService, responseSender),
                        UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new JWTFilter(tokenService), CustomBasicLoginFilter.class)
        .addFilterAfter(new SearchWorkspaceRoleFilter(new PathPatternParser(),workspaceJoinRepository), JWTFilter.class);



        // cors setting
        httpSecurity
                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));
                        return configuration;
                    }
                })));


        // oauth setting
        httpSecurity.oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                        .userService(customOAuth2UserService)).successHandler(new CustomOauthSuccessHandler(tokenService)));



        return httpSecurity.build();
    }
}
