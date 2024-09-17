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
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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

        if (headerAccessor == null || command == null) {
            return message;
        }

        switch (command) {
            case CONNECT -> {
                try {
                    String accessToken = WebSocketUtil.getAccessToken(headerAccessor);
                    DecodedJWT decodedJWT = authService.decodeToken(accessToken, "access");
                    String username = decodedJWT.getSubject();
                    Long channelId = WebSocketUtil.getChannelId(headerAccessor);

                    headerAccessor.getSessionAttributes().put("username", username);
                    headerAccessor.getSessionAttributes().put("channelId", channelId);

                    log.info("CONNECT: username={}, channelId={}", username, channelId);
                } catch (Exception e) {
                    log.error("Failed to process CONNECT command", e);
                    // 여기서 예외를 던져 연결을 중단할지, 그냥 로그만 남길지 결정할 수 있습니다.
                    throw new MessageDeliveryException("Failed to process CONNECT command");
                }
            }
            case SUBSCRIBE -> {
                try {
                    String username = (String) headerAccessor.getSessionAttributes().get("username");
                    Long channelId = (Long) headerAccessor.getSessionAttributes().get("channelId");

                    if (username == null || channelId == null) {
                        throw new MessageDeliveryException("Missing username or channelId");
                    }

                    boolean validated = channelService.validateEnterChannel(channelId, username);
                    headerAccessor.getSessionAttributes().put("validated", validated);
                    if (!validated) {
                        log.info("SUBSCRIBE: Validation failed for username={}, channelId={}", username, channelId);
                        throw new MessageDeliveryException("Validation failed for channel access");
                    }

                    log.info("SUBSCRIBE: username={}, channelId={}", username, channelId);
                } catch (Exception e) {
                    log.error("Failed to process SUBSCRIBE command", e);
                    throw new MessageDeliveryException("Failed to process SUBSCRIBE command");
                }
            }
            case DISCONNECT -> {
                if ((Boolean) headerAccessor.getSessionAttributes().get("validated")) {
                    String username = (String) headerAccessor.getSessionAttributes().get("username");
                    Long channelId = (Long) headerAccessor.getSessionAttributes().get("channelId");
                    if (!channelService.hasAccessTime(channelId, username)) {
                        channelService.createAccessTime(channelId, username);
                    }
                    channelService.updateAccessTime(channelId,username, LocalDateTime.now());

                }
            }
            default -> {
                // 다른 명령어는 별도로 처리하지 않고 그대로 메시지 반환
                return message;
            }
        }
        return message;
    }
}
