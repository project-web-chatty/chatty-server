package com.messenger.chatty.global.util;

import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.exception.custom.MemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
@Slf4j
public class WebSocketUtil {

    public static Long getChannelId(StompHeaderAccessor accessor) {
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

    public static String getAccessToken(StompHeaderAccessor accessor) {
        String token = accessor.getFirstNativeHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7).trim();
        }
        throw new MemberException(ErrorStatus.AUTH_NULL_TOKEN);
    }
}
