package com.messenger.chatty.global.config.web;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT")
        );
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .components(components);

    }
    private Info apiInfo() {
        return new Info()
                .title("WEB-CHATTY API DOCUMENTATION")
                .description("WEB CHATTY API 명세서입니다" +
                        " 로그인 엔드포인트에서 로그인 성공 시 response body에 담긴 엑세스 토큰을 'Authorize' 버튼을 눌렀을 시 나오는 Value에 입력해주세요. " +
                        "이후 요청의 Authorization header에는 엑세스 토큰이 자동으로 포함됩니다." +
                        " 토큰 재발급 요청에 한하여 헤더에 리프레시 토큰을 담아주세요")
                .version("1.0.0");
    }

}
