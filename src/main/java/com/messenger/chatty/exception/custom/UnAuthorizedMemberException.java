package com.messenger.chatty.exception.custom;



// 필터에서 반환할 exception
public class UnAuthorizedMemberException extends RuntimeException{
    public UnAuthorizedMemberException(String msg){
        super(msg);

    }
}
