package com.messenger.chatty.global.presentation.exception.custom;

import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.exception.GeneralException;

public class ChannelException extends GeneralException {
    public ChannelException(ErrorStatus errorStatus){
        super(errorStatus);
    }
}
