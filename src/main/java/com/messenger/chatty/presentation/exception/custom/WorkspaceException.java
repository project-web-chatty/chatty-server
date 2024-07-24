package com.messenger.chatty.presentation.exception.custom;

import com.messenger.chatty.presentation.ErrorStatus;
import com.messenger.chatty.presentation.exception.GeneralException;

public class WorkspaceException extends GeneralException {
    public WorkspaceException(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}
