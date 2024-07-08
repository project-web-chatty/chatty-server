package com.messenger.chatty.dto.response;

import lombok.Getter;


@Getter // response dto should include getter
public class ErrorResponseDTO {
    // private int errorCode;
    private final String message ;

    private ErrorResponseDTO(String msg){
        message = msg;
    }

    public static ErrorResponseDTO from(String message){
        return new ErrorResponseDTO(message);

    }

}
