package com.messenger.chatty.exception.custom;

public class DuplicatedNameException extends RuntimeException {
    public DuplicatedNameException(String message) {
        super(message);
    }
}
