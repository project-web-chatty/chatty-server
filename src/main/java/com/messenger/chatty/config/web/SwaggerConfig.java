package com.messenger.chatty.config.web;


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
                .description("WEB CHATTY API 명세서입니다. \n 엑세스토큰은 Authorization header, 리프레시 토큰은 쿠키에 발급됩니다." +
                        " 로그인 엔드포인트에서 로그인 성공 시 응답으로 받는 엑세스 토큰을(Bearer을 제외한 문자열) 'Authorize' 버튼을 눌렀을 시 나오는 Value에 입력해주세요. " +
                        "Value에 토큰이 설정되어 있는 동안에는 request의 Authorization header에 자동으로 엑세스 토큰이 포함됩니다. ")
                .version("1.0.0");
    }

}
