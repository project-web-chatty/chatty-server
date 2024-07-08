package com.messenger.chatty.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter // response dto should include getter
public class ErrorResponseDTO {
    // private int errorCode;
    private String message ;

    private ErrorResponseDTO(String msg){
        message = msg;
    }

    public static ErrorResponseDTO from(String message){
        return new ErrorResponseDTO(message);

    }

}
