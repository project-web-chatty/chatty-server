package com.messenger.chatty.presentation;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;

/*
according to the basic structure of the defaultErrorAttribute

* {
    "timestamp": "Tue Jul 09 22:34:07 KST 2024",
    "status": 400,
    "path": "/api/test/workspace/su",
    "errorDetail": "NO_SUCH_ELEMENT",
    "message": "there is no member whose username is su"
}
* */


@Getter // response dto should include getter
public class ErrorResponse {
    private final String timestamp;
    private final int status;
    private final String path;
    private final String errorDetail; // enum to string
    private final String message;


    private ErrorResponse(String errorPath , int status, String errorDetail, String message){
        this.timestamp = new Date().toString();
        this.status = status;
        this.path = errorPath;
        this.errorDetail = errorDetail;
        this.message = message;
    }

    public static ErrorResponse from(String errorPath, HttpStatus status, ErrorDetail detail, String message){
        return new ErrorResponse(errorPath, status.value(), detail.toString(), message);
    }


}
