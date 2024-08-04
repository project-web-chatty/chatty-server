package com.messenger.chatty.global.presentation.exception.custom;

import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.exception.GeneralException;

public class TokenException extends GeneralException {
    public TokenException(ErrorStatus errorStatus){
        super(errorStatus);
    }
}
