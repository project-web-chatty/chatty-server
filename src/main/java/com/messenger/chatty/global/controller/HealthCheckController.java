package com.messenger.chatty.global.controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "HEALTH CHECK API", description = "커넥션 체크용" )
@RestController
public class HealthCheckController {
    @GetMapping("/api/isHealthy")
    public String getHealthStatus(){
        return  "success";
    }


}

