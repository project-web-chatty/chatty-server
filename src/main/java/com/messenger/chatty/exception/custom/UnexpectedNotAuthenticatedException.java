package com.messenger.chatty.exception.custom;

public class UnexpectedNotAuthenticatedException extends RuntimeException{
    public UnexpectedNotAuthenticatedException(String msg){
        super(msg);
    }
}
