package com.messenger.chatty.presentation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorStatus {

    // 서버 오류
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, 5000, "서버 에러, 관리자에게 문의 바랍니다."),

    // 일반적인 요청 오류
    _BAD_REQUEST(BAD_REQUEST, 4000, "잘못된 요청입니다."),
    _UNAUTHORIZED(UNAUTHORIZED, 4001, "인증이 필요한 요청입니다."),
    _FORBIDDEN(FORBIDDEN, 4002, "금지된 요청입니다."),


    AUTH_NULL_TOKEN(BAD_REQUEST,4050,"헤더에 토큰이 없습니다"),
    AUTH_INVALID_TOKEN(BAD_REQUEST, 4051, "검증 결과 잘못된 토큰입니다."),
    AUTH_OUTDATED_REFRESH_TOKEN(BAD_REQUEST,4052,"갱신되기 이전의 리프레시 토큰입니다."),
    AUTH_EXPIRED_TOKEN(BAD_REQUEST, 4053, "토큰의 유효기간이 만료되었습니다."),
    AUTH_TYPE_MISMATCH_TOKEN(BAD_REQUEST,4054,"토큰의 타입이 맞지 않습니다."),

    AUTH_UNAUTHORIZED_ACCESS(FORBIDDEN,4013,"인증되었으나 해당 요청에 대한 권한이 부족합니다.");




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

