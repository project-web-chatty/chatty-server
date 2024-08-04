package com.messenger.chatty.global.presentation.exception.custom;

import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.exception.GeneralException;

public class WorkspaceException extends GeneralException {
    public WorkspaceException(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}
