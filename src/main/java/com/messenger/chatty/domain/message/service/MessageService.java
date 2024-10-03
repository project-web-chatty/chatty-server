package com.messenger.chatty.domain.message.service;

import com.messenger.chatty.domain.message.dto.request.MessageDto;
import com.messenger.chatty.domain.message.dto.response.MessageListDto;
import com.messenger.chatty.domain.message.dto.response.MessageResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {

    String send(MessageDto messageDto);

    List<MessageResponseDto> getMessageByLastAccessTime(Long channelId, Long workspaceJoinId, Pageable pageable);

    Long countUnreadMessage(Long channelId, Long workspaceJoinId);

    MessageListDto getMessages(Long channelId, Pageable pageable);

    String getLastReadMessageId(Long channelId, Long workspaceJoinId);

    MessageResponseDto getLastMessageInChannel(Long channelId);
}
