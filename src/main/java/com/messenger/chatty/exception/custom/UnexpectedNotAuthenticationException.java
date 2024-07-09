package com.messenger.chatty.exception.custom;

public class UnexpectedNotAuthenticationException extends RuntimeException{
    public UnexpectedNotAuthenticationException(String msg){
        super(msg);
    }
}
