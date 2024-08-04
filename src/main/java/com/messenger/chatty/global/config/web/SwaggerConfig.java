package com.messenger.chatty.global.config.web;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.chatty.global.presentation.ApiResponse;
import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.Reason;
import com.messenger.chatty.global.presentation.annotation.api.ApiErrorCodeExample;
import com.messenger.chatty.global.presentation.annotation.api.PredefinedErrorStatus;
import com.messenger.chatty.global.presentation.payload.ExampleHolder;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

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
        Server server = new Server().url("/");
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .addServersItem(server)
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
    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiErrorCodeExample apiErrorCodeExample =
                    handlerMethod.getMethodAnnotation(ApiErrorCodeExample.class);
            if (apiErrorCodeExample != null) {
                generateErrorCodeResponseExample(operation, apiErrorCodeExample.value(), apiErrorCodeExample.status());
            }
            return operation;
        };
    }

    private void generateErrorCodeResponseExample(
            Operation operation, ErrorStatus[] errorStatuses, PredefinedErrorStatus status) {
        ApiResponses responses = operation.getResponses();
        List<ErrorStatus> showErrorStatus = new ArrayList<>();
        showErrorStatus.addAll(status.getErrorStatuses());
        showErrorStatus.addAll(Arrays.asList(errorStatuses));

        Map<Integer, List<ExampleHolder>> statusWithExampleHolders =
                showErrorStatus.stream()
                        .map(
                                errorStatus -> {
                                    Reason errorReason = errorStatus.getReason();
                                    return ExampleHolder.builder()
                                            .holder(
                                                    getSwaggerExample(
                                                            errorReason.getMessage(),
                                                            errorReason))
                                            .code(errorReason.getCode())
                                            .name(errorReason.getCode().toString())
                                            .build();
                                })
                        .collect(groupingBy(ExampleHolder::getCode));

        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    private Example getSwaggerExample(String value, Reason errorReason) {
        ApiResponse<Object> responseDto = new ApiResponse<>(false, errorReason.getCode(), value, null);
        Example example = new Example();
        example.description(value);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonResponseDto = objectMapper.writeValueAsString(responseDto);
            example.setValue(jsonResponseDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return example;
    }

    private void addExamplesToResponses(
            ApiResponses responses, Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach(
                (status, v) -> {
                    Content content = new Content();
                    MediaType mediaType = new MediaType();
                    io.swagger.v3.oas.models.responses.ApiResponse apiResponse
                            = new io.swagger.v3.oas.models.responses.ApiResponse();
                    v.forEach(
                            exampleHolder -> mediaType.addExamples(
                                    exampleHolder.getName(), exampleHolder.getHolder()));
                    content.addMediaType("application/json", mediaType);
                    apiResponse.setContent(content);
                    responses.addApiResponse(status.toString(), apiResponse);
                });
    }

}
