package com.messenger.chatty.exception.custom;

public class InvalidInvitationCodeException extends RuntimeException{
    public InvalidInvitationCodeException(String msg){
        super(msg);
    }
}
