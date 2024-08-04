package com.messenger.chatty.global.presentation;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private final Boolean isSuccess;
    private final Integer code;
    private final String message;
    private final T result;

    public static <T> ApiResponse<T> onSuccess(T data) {
        return new ApiResponse<>(true, 2000, "성공", data);
    }


    public static <T> ApiResponse<T> of(Integer code, String message, T data) {
        return new ApiResponse<>(true, code,message, data);
    }
    public static <T> ApiResponse<T> onFailure(Integer code, String message, T errorDetail) {
        return new ApiResponse<>(false, code, message, errorDetail);
    }


}
