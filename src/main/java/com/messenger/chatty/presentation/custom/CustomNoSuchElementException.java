package com.messenger.chatty.presentation.custom;
import java.util.NoSuchElementException;

public class CustomNoSuchElementException extends NoSuchElementException {

    public CustomNoSuchElementException(String argumentType, String argument , String resourceType) {
        super(String.format("%s이 %s인 %s(은)는 존재하지 않습니다." ,argumentType,argument,resourceType ));
    }
   public CustomNoSuchElementException(String argumentType, Long argument, String resourceType){
       super(String.format("%s이 %d인 %s(은)는 존재하지 않습니다." ,argumentType,argument,resourceType ));
   }
   public CustomNoSuchElementException(String msg){
        super(msg);
   }
}
