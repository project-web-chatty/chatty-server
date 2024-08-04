package com.messenger.chatty.global.presentation.annotation.api;

import com.messenger.chatty.global.presentation.ErrorStatus;

import java.util.List;

public enum PredefinedErrorStatus {

    DEFAULT(List.of(ErrorStatus._INTERNAL_SERVER_ERROR)),
    AUTH(List.of(
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus.MEMBER_NOT_FOUND
    ));

    private final List<ErrorStatus> errorStatuses;

    PredefinedErrorStatus(List<ErrorStatus> errorStatuses) {
        this.errorStatuses = errorStatuses;
    }

    public List<ErrorStatus> getErrorStatuses() {
        return errorStatuses;
    }
}
