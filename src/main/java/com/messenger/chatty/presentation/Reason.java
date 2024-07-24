package com.messenger.chatty.presentation;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;


// Reason 객체는 apiResponse를 만드는데 중요하게 사용
@Getter
@Builder
public class Reason {
    private HttpStatus httpStatus;
    private final boolean isSuccess;
    private final Integer code;
    private final String message;
    private final HashMap<String, String> data;
}
