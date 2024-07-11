package com.messenger.chatty.exception.custom;

public class PasswordInEqualityException extends RuntimeException {
    public PasswordInEqualityException(String msg){
        super(msg);
    }
}
