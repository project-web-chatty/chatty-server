package com.messenger.chatty.global.config.stomp;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.messenger.chatty.domain.channel.service.ChannelService;
import com.messenger.chatty.global.util.WebSocketUtil;
import com.messenger.chatty.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Component
@RequiredArgsConstructor
@Slf4j
public class ChatPreHandler implements ChannelInterceptor {

    private final AuthService authService;
    private final ChannelService channelService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        StompCommand command = headerAccessor.getCommand();
        log.info("message = {}", message.getPayload());
        log.info("message header = {}", message.getHeaders());

        if (command != null) {
            switch (command) {
                case CONNECT -> {
                    DecodedJWT decodedJWT = authService.decodeToken(WebSocketUtil.getAccessToken(headerAccessor), "access");
                    String username =  decodedJWT.getSubject();
                    Long channelId = WebSocketUtil.getChannelId(headerAccessor);
                    boolean validated = channelService.validateEnterChannel(channelId, username);
                    if (!validated) {
                        log.info("validation failed");
                        //TODO exception of chat
                    }
                    headerAccessor.getSessionAttributes().put("username", username);
                    headerAccessor.getSessionAttributes().put("channelId", channelId);
                }
                case SUBSCRIBE -> {
                    String destination = headerAccessor.getDestination();
                    String username = String.valueOf(headerAccessor.getSessionAttributes().get("username"));
                    Long channelId = Long.valueOf(String.valueOf(headerAccessor.getSessionAttributes().get("channelId")));
                    log.info("username = {}", username);
                    log.info("SUBSCRIBE destination = {}", destination);
                    log.info("channelId extracted = {}", channelId);

                    // 추가적인 검증 로직
                    if (channelId == null || !channelService.validateEnterChannel(channelId, username)) {
                        log.error("Invalid channelId or access denied: channelId = {}", channelId);
                        throw new MessageDeliveryException("Access denied or invalid channel ID");
                    }
                }
                default -> {
                    return message;
                }
            }
        }else{
            SimpMessageType messageType = headerAccessor.getMessageType();
            switch (messageType) {
                case HEARTBEAT -> {
                    log.info("heartbeat");
                }
                default -> {
                    return message;
                }
            }
        }
        return message;
    }
}
