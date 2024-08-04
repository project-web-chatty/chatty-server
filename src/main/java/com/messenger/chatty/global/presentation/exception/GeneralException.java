package com.messenger.chatty.global.presentation.exception;

import com.messenger.chatty.global.presentation.Reason;
import com.messenger.chatty.global.presentation.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException{
    private final ErrorStatus errorStatus;

    @Override
    public String getMessage() {
        return errorStatus.getReason().getMessage();
    }

    public Reason getErrorReason() {
        return this.errorStatus.getReason();
    }
}