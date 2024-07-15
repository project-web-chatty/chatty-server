package com.messenger.chatty.exception.custom;

public class InvalidRequestParamException extends RuntimeException{
    public InvalidRequestParamException(String msg){
        super(msg);
    }
}
