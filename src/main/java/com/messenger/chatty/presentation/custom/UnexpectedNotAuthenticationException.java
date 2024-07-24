package com.messenger.chatty.presentation.custom;

public class UnexpectedNotAuthenticationException extends RuntimeException{
    public UnexpectedNotAuthenticationException(String msg){
        super(msg);
    }
}
