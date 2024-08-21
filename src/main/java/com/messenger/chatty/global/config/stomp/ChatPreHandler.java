package com.messenger.chatty.global.config.stomp;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.messenger.chatty.domain.channel.service.ChannelService;
import com.messenger.chatty.domain.member.dto.response.MyProfileDto;
import com.messenger.chatty.domain.member.service.MemberService;
import com.messenger.chatty.domain.refresh.dto.response.TokenResponseDto;
import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.exception.GeneralException;
import com.messenger.chatty.global.presentation.exception.custom.MemberException;
import com.messenger.chatty.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.messaging.simp.stomp.StompCommand.*;
import static org.springframework.messaging.simp.stomp.StompCommand.UNSUBSCRIBE;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Component
@RequiredArgsConstructor
@Slf4j
public class ChatPreHandler implements ChannelInterceptor {

    private final AuthService authService;
    private final MemberService memberService;
    private final ChannelService channelService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        StompCommand command = headerAccessor.getCommand();

        //only entrance
        if(command.equals(SUBSCRIBE) || command.equals(CONNECT)) {
            DecodedJWT decodedJWT = authService.decodeToken(getAccessToken(headerAccessor), "access");
            String username =  decodedJWT.getSubject();
            Long channelId = getChannelId(headerAccessor);
            boolean validated = channelService.validateEnterChannel(channelId, username);
            if (!validated) {
                //TODO exception of chat
            }
        }
        return message;
    }

    private String getAccessToken(StompHeaderAccessor accessor) {
        String token = accessor.getFirstNativeHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7).trim();
        }
        throw new MemberException(ErrorStatus.AUTH_NULL_TOKEN);
    }

    private Long getChannelId(StompHeaderAccessor accessor) {
        String channelIdStr = accessor.getFirstNativeHeader("channelId");
        if (channelIdStr != null) {
            try {
                return Long.valueOf(channelIdStr);
            } catch (NumberFormatException e) {
                log.error("Invalid chatRoomId format: {}", channelIdStr, e);
            }
        }
        return null;
    }

}
