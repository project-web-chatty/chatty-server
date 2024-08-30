package com.messenger.chatty.domain.message.service;

import com.messenger.chatty.domain.message.dto.MessageDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {

    String send(MessageDto messageDto);

    List<MessageDto> getMessageByLastAccessTime(Long channelId, String username, Pageable pageable);

    Long countUnreadMessage(Long channelId, String username);
}
