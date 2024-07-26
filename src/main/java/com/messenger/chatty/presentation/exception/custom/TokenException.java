package com.messenger.chatty.presentation.exception.custom;

import com.messenger.chatty.presentation.ErrorStatus;
import com.messenger.chatty.presentation.exception.GeneralException;

public class TokenException extends GeneralException {
    public TokenException(ErrorStatus errorStatus){
        super(errorStatus);
    }
}
