package com.messenger.chatty.security.config;


import com.messenger.chatty.domain.workspace.repository.WorkspaceJoinRepository;
import com.messenger.chatty.security.filter.CustomAccessDeniedHandler;
import com.messenger.chatty.security.filter.CustomAuthenticationEntryPoint;
import com.messenger.chatty.security.filter.SearchWorkspaceRoleFilter;
import com.messenger.chatty.security.filter.JWTFilter;
import com.messenger.chatty.security.oauth2.CustomOAuth2UserService;
import com.messenger.chatty.security.oauth2.CustomOauth2FailureHandler;
import com.messenger.chatty.security.oauth2.CustomOauth2SuccessHandler;
import com.messenger.chatty.security.service.AuthService;
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
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Configuration
@EnableWebSecurity // (debug = true) it should not be included in deployment ver
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthService authService;
    private final WorkspaceJoinRepository workspaceJoinRepository;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;


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
                        .requestMatchers("/api/auth/password").authenticated()
                        .requestMatchers("/v3/**", "/swagger-ui/**", "/api/isHealthy",
                                "/api/member/signup", "/api/member/check", "/api/auth/**")
                        .permitAll()
                        .requestMatchers("/api/workspace/join/**", "/api/workspace").authenticated()
                        .requestMatchers("/chat/**", "/stomp/chat/**", "/pub/chat/**",
                                "/message/**", "/queue/**", "/topic/**", "/socket.io/**",
                                "/exchange/**", "/amq/queue/**").permitAll()    //임시
                        .requestMatchers(HttpMethod.GET, "/api/workspace/**").hasAnyRole("ADMIN", "WORKSPACE_OWNER", "WORKSPACE_MEMBER")
                        .requestMatchers("/api/workspace/**").hasAnyRole("ADMIN", "WORKSPACE_OWNER")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated());

        // custom filters settings
        httpSecurity
                .addFilterBefore(new JWTFilter(authService), AnonymousAuthenticationFilter.class)
                .addFilterAfter(new SearchWorkspaceRoleFilter(new PathPatternParser(), workspaceJoinRepository), JWTFilter.class);

        // CORS 설정
        httpSecurity
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(List.of(
                                "http://ec2-3-34-211-45.ap-northeast-2.compute.amazonaws.com:3000"
                        ));
                        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));
                        return configuration;
                    }
                }));



        // oauth2 setting
        httpSecurity.oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                        .userService(customOAuth2UserService)).successHandler(new CustomOauth2SuccessHandler(authService))
                .failureHandler(new CustomOauth2FailureHandler()) );



        return httpSecurity.build();
    }
}
