package com.messenger.chatty.presentation.custom;

public class InvalidRequestParamException extends RuntimeException{
    public InvalidRequestParamException(String msg){
        super(msg);
    }
}
