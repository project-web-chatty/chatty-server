package com.messenger.chatty.exception;

import lombok.Getter;


@Getter // response dto should include getter
public class ErrorResponse {
    private final String errorType;
    private final String message ;

    private ErrorResponse(String errorType, String message){
        this.errorType = errorType;
        this.message = message;
    }

    public static ErrorResponse from(ErrorType type, String msg){
        return new ErrorResponse(type.toString() ,msg);
    }

}
