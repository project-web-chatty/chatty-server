package com.messenger.chatty.global.presentation.exception.custom;

import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.exception.GeneralException;

public class S3Exception extends GeneralException {
    public S3Exception(ErrorStatus errorStatus){    super(errorStatus);    }
}
