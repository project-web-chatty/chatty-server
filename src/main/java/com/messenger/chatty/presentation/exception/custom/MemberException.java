package com.messenger.chatty.presentation.exception.custom;

import com.messenger.chatty.presentation.ErrorStatus;
import com.messenger.chatty.presentation.exception.GeneralException;

public class MemberException extends GeneralException {
    public MemberException(ErrorStatus errorStatus){
        super(errorStatus);
    }
}
