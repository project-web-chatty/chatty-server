package com.messenger.chatty.exception.custom;



// 필터에서 반환할 exception 임시로 적어놓음
public class UnAuthorizedMemberException extends RuntimeException{

    public UnAuthorizedMemberException(String msg){
        super(msg);
    }
}
