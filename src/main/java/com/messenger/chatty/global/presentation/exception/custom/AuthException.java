package com.messenger.chatty.global.presentation.exception.custom;

import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.exception.GeneralException;

public class AuthException extends GeneralException {
    public AuthException(ErrorStatus errorStatus){
        super(errorStatus);
    }
}
