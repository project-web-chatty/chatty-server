package com.messenger.chatty.domain.message.service;

import com.messenger.chatty.domain.message.dto.request.MessageDto;
import com.messenger.chatty.domain.message.dto.response.MessageResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {

    String send(MessageDto messageDto);

    List<MessageResponseDto> getMessageByLastAccessTime(Long channelId, String username, Pageable pageable);

    Long countUnreadMessage(Long channelId, String username);

    List<MessageResponseDto> getMessages(Long channelId, Pageable pageable);

    String getLastReadMessageId(Long channelId, String username);

    MessageResponseDto getLastMessageInChannel(Long channelId);
}
