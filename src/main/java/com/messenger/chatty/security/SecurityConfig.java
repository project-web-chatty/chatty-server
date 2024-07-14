package com.messenger.chatty.security;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;


@Configuration
@EnableWebSecurity // (debug = true) it should not be included in deployment ver
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthService authService;
    private final AuthenticationConfiguration authenticationConfiguration;


    @Bean // for encoding password
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
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
                .cors(Customizer.withDefaults())
                .logout(AbstractHttpConfigurer::disable);

        // authorization setting
        httpSecurity
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/v3/**", "/swagger-ui/**", "/api/isHealthy",
                                "/api/member/**","/api/auth/reissue").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("api/workspace/join/**").authenticated()
                        .requestMatchers(HttpMethod.POST,"/api/workspace/**","/api/invite/**")
                        .hasAnyRole("ADMIN","WORKSPACE_OWNER")
                        .requestMatchers(HttpMethod.PUT,"/api/workspace/**")
                        .hasAnyRole("ADMIN","WORKSPACE_OWNER")
                        .requestMatchers(HttpMethod.DELETE,"/api/workspace/**")
                        .hasAnyRole("ADMIN","WORKSPACE_OWNER")
                        .requestMatchers(HttpMethod.GET,"/api/workspace/**","/api/invite/**").hasAnyRole("ADMIN","WORKSPACE_OWNER","WORKSPACE_MEMBER")
                        .anyRequest().authenticated()); // 나머지 엔드포인트에 대해서는 인증만 요구

        // add custom filters
        httpSecurity.addFilterAt(new BasicLoginFilter(authenticationManager(authenticationConfiguration), authService), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new JWTFilter(authService), BasicLoginFilter.class)
                .addFilterAt(new CustomLogoutFilter(authService),LogoutFilter.class);

        // cors setting
        httpSecurity
                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));
                        return configuration;
                    }
                })));


        return httpSecurity.build();
    }
}
