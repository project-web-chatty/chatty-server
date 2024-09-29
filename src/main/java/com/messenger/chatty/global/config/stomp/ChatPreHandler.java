package com.messenger.chatty.global.config.stomp;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.messenger.chatty.domain.channel.service.ChannelService;
import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.exception.GeneralException;
import com.messenger.chatty.global.presentation.exception.custom.StompMessagingException;
import com.messenger.chatty.global.util.WebSocketUtil;
import com.messenger.chatty.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
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
            case STOMP -> {
            }
            case CONNECT -> {
                try {
                    String accessToken = WebSocketUtil.getAccessToken(headerAccessor);
                    DecodedJWT decodedJWT = authService.decodeToken(accessToken, "access");
                    String username = decodedJWT.getSubject();
                    Long channelId = WebSocketUtil.getChannelId(headerAccessor);
                    //TODO username & channelId -> workspaceJoinId
                    Long workspaceJoinId = channelService.getWorkspaceJoinId(channelId, username);

                    headerAccessor.getSessionAttributes().put("workspaceJoinId", workspaceJoinId);


                    log.info("CONNECT: username={}, channelId={}", username, channelId);
                } catch (RuntimeException e) {
                    log.error("Failed to process CONNECT command", e);
                    throw new StompMessagingException(ErrorStatus.AUTH_INVALID_TOKEN);
                }
            }
            case SUBSCRIBE -> {
                String username = (String) headerAccessor.getSessionAttributes().get("username");
                Long channelId = (Long) headerAccessor.getSessionAttributes().get("channelId");
                if (username == null || channelId == null) {
                    throw new StompMessagingException(ErrorStatus.REQUEST_PARAM_IS_NULL);
                }

                boolean validated;
                try{
                    validated = channelService.validateEnterChannel(channelId, username);
                }
                catch (GeneralException e){
                    throw new StompMessagingException(ErrorStatus.INVALID_REQUEST_PARAM);
                }

                headerAccessor.getSessionAttributes().put("validated", validated);
                if (!validated) {
                    log.info("SUBSCRIBE: Validation failed for username={}, channelId={}", username, channelId);
                    throw new StompMessagingException(ErrorStatus.CHANNEL_ACCESS_DENIAL);
                }
                log.info("SUBSCRIBE: username={}, channelId={}", username, channelId);

            }
            case DISCONNECT -> {
                try{
                    if ((Boolean) headerAccessor.getSessionAttributes().get("validated")) {
                        String username = (String) headerAccessor.getSessionAttributes().get("username");
                        Long channelId = (Long) headerAccessor.getSessionAttributes().get("channelId");
                        if (!channelService.hasAccessTime(channelId, username)) {
                            channelService.createAccessTime(channelId, username);
                        }
                        channelService.updateAccessTime(channelId,username, LocalDateTime.now());

                    }
                }
                catch (RuntimeException e){
                    throw new StompMessagingException(ErrorStatus.INVALID_DISCONNECT_LOGIC);
                    // 예외 발생시 수정 요망
                }

            }
            case UNSUBSCRIBE -> {
            }
            case SEND -> {
            }
            case ACK -> {
            }
            case NACK -> {
            }
            case BEGIN -> {
            }
            case COMMIT -> {
            }
            case ABORT -> {
            }
            case CONNECTED -> {
            }
            case RECEIPT -> {
            }
            case MESSAGE -> {
            }
            case ERROR -> {
            }
            default -> {
                // 다른 명령어는 별도로 처리하지 않고 그대로 메시지 반환
                return message;
            }
        }
        return message;
    }
}
