package com.messenger.chatty.global.presentation.exception.custom;

import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.exception.GeneralException;

public class StompMessagingException extends GeneralException {
    public StompMessagingException(ErrorStatus errorStatus){
        super(errorStatus);
    }
}
