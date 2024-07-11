package com.messenger.chatty.config.aws;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


// health check from ELB
@RestController
public class HealthCheckController {
    @GetMapping("/api/isHealthy")
    public String getHealthStatus(){
        return  "success";
    }

}

