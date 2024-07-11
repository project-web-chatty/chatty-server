package com.messenger.chatty.exception.custom;

public class DuplicatedNameException extends RuntimeException {
    public DuplicatedNameException( String argument, String argumentType) {
        super(String.format("%s(은)는 중복되는 %s입니다.",argument,argumentType));
    }
    public DuplicatedNameException(Long argument,String argumentType) {
        super(String.format("%d(은)는 중복되는 %s입니다.",argument,argumentType));

    }
    public DuplicatedNameException(String msg){
        super(msg);
    }

}
