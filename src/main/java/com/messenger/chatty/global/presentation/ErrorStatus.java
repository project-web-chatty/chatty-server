package com.messenger.chatty.global.presentation;

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

    // common
    COMMON_INVALID_ARGUMENT(BAD_REQUEST,4010,"잘못된 형식의 요청 필드가 있습니다"),
    COMMON_CONSTRAINT_VIOLATION(BAD_REQUEST,4011,"DB 저장 시 제약조건에 위배되는 요청입니다."),

    // token & auth error
    AUTH_NULL_TOKEN(BAD_REQUEST,4050,"헤더에 토큰이 없습니다"),
    AUTH_INVALID_TOKEN(BAD_REQUEST, 4051, "검증 결과 잘못된 토큰입니다."),
    AUTH_OUTDATED_REFRESH_TOKEN(BAD_REQUEST,4052,"갱신되기 이전의 리프레시 토큰입니다."),
    AUTH_EXPIRED_TOKEN(BAD_REQUEST, 4053, "토큰의 유효기간이 만료되었습니다."),
    AUTH_TYPE_MISMATCH_TOKEN(BAD_REQUEST,4054,"토큰의 타입이 맞지 않습니다."),
    AUTH_UNAUTHORIZED_ACCESS(FORBIDDEN,4055,"인증되었으나 해당 요청에 대한 권한이 부족합니다."),
    AUTH_FAIL_LOGIN(FORBIDDEN,4056,"아이디 또는 비밀번호를 잘못 입력하였습니다."),

    // workspace error
    WORKSPACE_NOT_FOUND(BAD_REQUEST,4060,"해당 워크스페이스가 존재하지 않습니다."),
    WORKSPACE_NAME_ALREADY_EXISTS(BAD_REQUEST,4061,"해당 워크스페이스 이름은 이미 존재합니다."),
    WORKSPACE_INVALID_INVITATION_CODE(BAD_REQUEST,4062,"해당 초대코드는 유효하지 않습니다."),
    WORKSPACE_INVALID_ROLE_CHANGE_REQUEST(BAD_REQUEST,4063,"잘못된 ROLE 변경 요청입니다."),
    WORKSPACE_UNAUTHORIZED(BAD_REQUEST, 4064, "해당 워크스페이스에 권한 없는 요청입니다."),
    // channel error
    CHANNEL_NOT_FOUND(BAD_REQUEST,4070,"해당 채널이 존재하지 않습니다."),
    CHANNEL_NOT_IN_WORKSPACE(BAD_REQUEST,4071,"해당 워크스페이스 내에 소속된 채널이 아닙니다."),
    CHANNEL_NAME_ALREADY_EXISTS(BAD_REQUEST,4072,"해당 채널 이름은 이미 존재합니다."),
    CHANNEL_ACCESS_NOT_FOUND(NOT_FOUND, 4073, "채널 접근이 조회되지 않습니다"),

    // member error
    MEMBER_NOT_FOUND(BAD_REQUEST,4080,"해당 멤버는 존재하지 않습니다."),
    MEMBER_USERNAME_ALREADY_EXISTS(BAD_REQUEST,4081,"해당 USERNAME은 이미 존재합니다."),
    MEMBER_ALREADY_EXISTS_IN_WORKSPACE(BAD_REQUEST,4082,"해당 멤버는 워크스페이스에 이미 존재합니다."),
    MEMBER_NOT_IN_WORKSPACE(BAD_REQUEST,4083,"해당 멤버는 워크스페이스에 존재하지 않습니다."),

    // stomp 전용 error
    INVALID_CONNECT_REQUEST(BAD_REQUEST,4901,"유효한 토큰을 포함하여 CONNECT 요청을 보내주세요."),
    REQUEST_PARAM_IS_NULL(BAD_REQUEST,4902, "유저네임 또는 채널 아이디를 포함하여 요청하세요."),
    INVALID_REQUEST_PARAM(BAD_REQUEST,4903, "유효하지 않은 유저네임 또는 채널 아이디입니다."),
    CHANNEL_ACCESS_DENIAL(BAD_REQUEST,4904, "해당 채널에 접근할 수 없습니다."),
    INVALID_DISCONNECT_LOGIC(INTERNAL_SERVER_ERROR,5900, "DISCONNECT 로직에서 문제가 발생하였습니다.");




    private final HttpStatus httpStatus;
    private final Integer code;
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

