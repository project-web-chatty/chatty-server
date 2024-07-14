package com.messenger.chatty.config.aws;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

