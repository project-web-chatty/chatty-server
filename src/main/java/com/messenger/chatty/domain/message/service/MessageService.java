package com.messenger.chatty.domain.message.service;

import com.messenger.chatty.domain.message.dto.MessageDto;
import com.messenger.chatty.domain.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {

    String send(MessageDto messageDto);

    Page<Message> getMessageByLastAccessTime(Long channelId, String username, Pageable pageable);

    Long countUnreadMessage(Long channelId, String username);
}
