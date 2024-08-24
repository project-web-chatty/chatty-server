package com.messenger.chatty.global.config.stomp;

import com.messenger.chatty.domain.channel.service.ChannelService;
import com.messenger.chatty.global.util.WebSocketUtil;
import com.messenger.chatty.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebsocketEventListener {

    private final ChannelService channelService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        Long channelId = WebSocketUtil.getChannelId(headerAccessor);
        if (username != null) {
            channelService.updateAccessTime(channelId, username);
        }

    }
}
