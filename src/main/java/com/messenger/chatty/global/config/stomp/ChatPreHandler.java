package com.messenger.chatty.global.config.stomp;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.messenger.chatty.domain.member.service.MemberService;
import com.messenger.chatty.domain.refresh.dto.response.TokenResponseDto;
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

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String authorizationHeader = String.valueOf(headerAccessor.getNativeHeader("Authorization"));
        StompCommand command = headerAccessor.getCommand();


        if(command.equals(SUBSCRIBE) || command.equals(CONNECT) || command.equals(SEND)) {
            DecodedJWT decodedJWT = authService.decodeToken(authorizationHeader, "access");
            TokenResponseDto token = authService
                    .generateTokenPair(decodedJWT.getSubject(), decodedJWT.getClaim("role").asString());
        }
        return message;
    }

}
