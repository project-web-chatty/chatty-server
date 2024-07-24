package com.messenger.chatty.presentation.custom;

public class InvalidInvitationCodeException extends RuntimeException{
    public InvalidInvitationCodeException(String msg){
        super(msg);
    }
}
