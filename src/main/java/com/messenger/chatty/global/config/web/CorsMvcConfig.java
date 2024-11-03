package com.messenger.chatty.global.config.web;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**")
                .allowedOrigins("http://ec2-3-34-211-45.ap-northeast-2.compute.amazonaws.com:3000","http://localhost:3000", "https://apic.app");
    }
}
