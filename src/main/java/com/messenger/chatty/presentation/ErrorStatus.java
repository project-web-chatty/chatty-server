package com.messenger.chatty.presentation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorStatus {


    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, 5000, "서버 에러, 관리자에게 문의 바랍니다."),
    // 일반적인 요청 오류
    _BAD_REQUEST(BAD_REQUEST, 4000, "잘못된 요청입니다."),
    //인증 관련 오류(4050 ~ 4099)

    AUTH_INVALID_TOKEN(BAD_REQUEST, 4050, "유효하지 않은 토큰입니다."),
    AUTH_UNAUTHORIZED_ACCESS(FORBIDDEN,4003,"인증되었으나 해당 요청에 대한 권한이 부족합니다."),
    AUTH_UNAUTHENTICATED(UNAUTHORIZED,4001,"인증되지 않았습니다.");

    // 일반적인 status
    private final HttpStatus httpStatus;
    // 커스텀 코드
    private final Integer code;
    // 에러 메시지
    private final String message;


    public Reason getReason() {
        return Reason.builder()
                .message(message)
                .code(code)
                .httpStatus(httpStatus)
                .isSuccess(false)
                .build();
    }


}

