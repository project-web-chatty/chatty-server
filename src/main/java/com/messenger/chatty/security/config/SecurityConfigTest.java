//package com.messenger.chatty.security.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity // (debug = true) it should not be included in deployment ver
//@EnableMethodSecurity
//@RequiredArgsConstructor
//public class SecurityConfigTest {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//
//        // stateless session
//        httpSecurity.sessionManagement((sessionManagementConfigurer ->
//                sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)));
//
//        // default setting
//        httpSecurity
//                .csrf(AbstractHttpConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable)
////                .httpBasic(AbstractHttpConfigurer::disable)
//                .logout(AbstractHttpConfigurer::disable);
//
//        httpSecurity
//                .authorizeHttpRequests((auth) -> auth
//                        .anyRequest().permitAll());
//
//        return httpSecurity.build();
//    }
//}
