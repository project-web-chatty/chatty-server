package com.messenger.chatty.presentation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorStatus {

    // server error
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, 5000, "서버 오류입니다."),
    _UNINTENDED_AUTHENTICATION_ERROR(INTERNAL_SERVER_ERROR,5001,"서버 에러로 인증에 실패하였습니다."),

    // default
    _BAD_REQUEST(BAD_REQUEST, 4000, "잘못된 요청입니다."),
    _UNAUTHORIZED(UNAUTHORIZED, 4001, "인증이 필요한 요청입니다."),
    _FORBIDDEN(FORBIDDEN, 4002, "금지된 요청입니다."),



    // token & auth error
    AUTH_NULL_TOKEN(BAD_REQUEST,4050,"헤더에 토큰이 없습니다"),
    AUTH_INVALID_TOKEN(BAD_REQUEST, 4051, "검증 결과 잘못된 토큰입니다."),
    AUTH_OUTDATED_REFRESH_TOKEN(BAD_REQUEST,4052,"갱신되기 이전의 리프레시 토큰입니다."),
    AUTH_EXPIRED_TOKEN(BAD_REQUEST, 4053, "토큰의 유효기간이 만료되었습니다."),
    AUTH_TYPE_MISMATCH_TOKEN(BAD_REQUEST,4054,"토큰의 타입이 맞지 않습니다."),
    AUTH_UNAUTHORIZED_ACCESS(FORBIDDEN,4013,"인증되었으나 해당 요청에 대한 권한이 부족합니다."),

    // workspace error
    WORKSPACE_NOT_FOUND(BAD_REQUEST,4061,"해당 워크스페이스가 존재하지 않습니다."),
    WORKSPACE_NAME_ALREADY_EXISTS(BAD_REQUEST,4062,"해당 워크스페이스 이름은 이미 존재합니다."),
    WORKSPACE_INVALID_INVITATION_CODE(BAD_REQUEST,4063,"해당 초대코드는 유효하지 않습니다."),
    WORKSPACE_INVALID_ROLE_CHANGE_REQUEST(BAD_REQUEST,4064,"잘못된 ROLE 변경 요청입니다."),
    // channel error
    CHANNEL_NOT_FOUND(BAD_REQUEST,4071,"해당 채널이 존재하지 않습니다."),
    CHANNEL_NOT_IN_WORKSPACE(BAD_REQUEST,4072,"해당 워크스페이스 내에 소속된 채널이 아닙니다."),
    CHANNEL_NAME_ALREADY_EXISTS(BAD_REQUEST,4073,"해당 채널 이름은 이미 존재합니다."),

    // member error
    MEMBER_NOT_FOUND(BAD_REQUEST,4081,"해당 멤버는 존재하지 않습니다."),
    MEMBER_USERNAME_ALREADY_EXISTS(BAD_REQUEST,4082,"해당 USERNAME은 이미 존재합니다."),
    MEMBER_ALREADY_EXISTS_IN_WORKSPACE(BAD_REQUEST,4083,"해당 멤버는 워크스페이스에 이미 존재합니다."),
    MEMBER_NOT_IN_WORKSPACE(BAD_REQUEST,4084,"해당 멤버는 워크스페이스에 존재하지 않습니다.");


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

