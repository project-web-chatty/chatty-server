package com.messenger.chatty.presentation.exception.custom;

import com.messenger.chatty.presentation.ErrorStatus;
import com.messenger.chatty.presentation.exception.GeneralException;

public class AuthException extends GeneralException {
    public AuthException(ErrorStatus errorStatus){
        super(errorStatus);
    }
}
