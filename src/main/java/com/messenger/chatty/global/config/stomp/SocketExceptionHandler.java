package com.messenger.chatty.global.config.stomp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.messenger.chatty.global.presentation.ApiResponse;
import com.messenger.chatty.global.presentation.GlobalExceptionHandler;
import com.messenger.chatty.global.presentation.exception.custom.StompMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;


@Component
@RequiredArgsConstructor
@Slf4j
public class SocketExceptionHandler extends StompSubProtocolErrorHandler {
     private final GlobalExceptionHandler globalExceptionHandler;
    private final ObjectMapper objectMapper;

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]>clientMessage, Throwable ex)
    {
        Throwable cause = ex.getCause();

        try {
            if (cause instanceof StompMessagingException) {
                log.info("handled error exception when stomp Messaging : {}" , ((StompMessagingException) cause).getMessage());
                return prepareErrorMessage(globalExceptionHandler.handleStompMessageException((StompMessagingException) cause));
            }

        }catch (Exception e) {
                log.error("error during processing websocket exception : {}",e.getMessage());
                return super.handleClientMessageProcessingError(clientMessage, e);
        }

        log.info("unhandled error exception when stomp Messaging : {}" , ((Exception) cause).getMessage());
        return super.handleClientMessageProcessingError(clientMessage, ex);
    }


    //    // 메세지 생성
    private Message<byte[]> prepareErrorMessage(ApiResponse failResponse) throws JsonProcessingException {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setLeaveMutable(true);
        return MessageBuilder.createMessage(objectMapper.writeValueAsString(failResponse).getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }
}
