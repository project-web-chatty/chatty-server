package com.messenger.chatty.global.presentation.exception.custom;

import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.exception.GeneralException;

public class MemberException extends GeneralException {
    public MemberException(ErrorStatus errorStatus){
        super(errorStatus);
    }
}
