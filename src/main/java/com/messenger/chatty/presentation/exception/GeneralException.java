package com.messenger.chatty.presentation.exception;

import com.messenger.chatty.presentation.ErrorStatus;
import com.messenger.chatty.presentation.Reason;
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