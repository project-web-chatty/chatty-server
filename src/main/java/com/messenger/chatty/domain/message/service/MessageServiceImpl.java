package com.messenger.chatty.domain.message.service;

import com.messenger.chatty.domain.channel.entity.ChannelAccess;
import com.messenger.chatty.domain.channel.repository.ChannelAccessRepository;
import com.messenger.chatty.domain.message.dto.MessageDto;
import com.messenger.chatty.domain.message.entity.Message;
import com.messenger.chatty.domain.message.repository.MessageRepository;
import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.exception.custom.ChannelException;
import com.messenger.chatty.global.util.CustomConverter;
import com.messenger.chatty.global.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService{
    private final MessageRepository messageRepository;
    private final ChannelAccessRepository channelAccessRepository;

    @Override
    public String send(MessageDto messageDto) {
        Message message = Message.of(messageDto);
        messageRepository.save(message);
        return message.getId();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MessageDto> getMessageByLastAccessTime(Long channelId, String username, Pageable pageable) {
        ChannelAccess channelAccess = channelAccessRepository.findChannelAccessByChannel_IdAndUsername(channelId, username)
                .orElseThrow(() -> new ChannelException(ErrorStatus.CHANNEL_ACCESS_NOT_FOUND));
        Page<Message> messages = messageRepository
                .findMessagesByChatRoomIdAfterMemberDisconnectTime(
                        channelId,
                        TimeUtil.convertTimeTypeToLong(channelAccess.getLastModifiedDate()),
                        pageable);
        return CustomConverter.convertMessageResponse(messages);
    }

    @Transactional(readOnly = true)
    @Override
    public Long countUnreadMessage(Long channelId, String username) {
        ChannelAccess channelAccess = channelAccessRepository.findChannelAccessByChannel_IdAndUsername(channelId, username)
                .orElseThrow(() -> new ChannelException(ErrorStatus.CHANNEL_ACCESS_NOT_FOUND));

        return messageRepository.countByChatRoomIdAndSendTimeAfter(
                channelId,
                TimeUtil.convertTimeTypeToLong(channelAccess.getLastModifiedDate())
        );
    }

    @Override
    public List<MessageDto> getMessages(Long channelId, Pageable pageable) {
        Page<Message> messages = messageRepository.findMessages(channelId, pageable);
        return CustomConverter.convertMessageResponse(messages);
    }

    @Transactional(readOnly = true)
    @Override
    public String getLastUnreadMessageId(Long channelId, String username) {
        ChannelAccess channelAccess = channelAccessRepository
                .findChannelAccessByChannel_IdAndUsername(channelId, username)
                .orElseThrow(() -> new ChannelException(ErrorStatus.CHANNEL_ACCESS_NOT_FOUND));
        return channelAccess.getLastMessageId();
    }

    @Override
    public MessageDto getLastMessageInChannel(Long channelId) {
        Pageable pageable = PageRequest.of(0, 1);
        return CustomConverter.convertMessageResponse(
                        messageRepository.findMessages(channelId, pageable)
                ).get(0);
    }
}
