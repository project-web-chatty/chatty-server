package com.messenger.chatty.presentation.exception.custom;

import com.messenger.chatty.presentation.ErrorStatus;
import com.messenger.chatty.presentation.exception.GeneralException;

public class ChannelException extends GeneralException {
    public ChannelException(ErrorStatus errorStatus){
        super(errorStatus);
    }
}
