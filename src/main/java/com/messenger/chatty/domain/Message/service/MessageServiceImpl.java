package com.messenger.chatty.domain.Message.service;

import com.messenger.chatty.domain.Message.dto.MessageDto;
import com.messenger.chatty.domain.Message.entity.Message;
import com.messenger.chatty.domain.Message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService{
    private final MessageRepository messageRepository;
    @Override
    public String send(MessageDto messageDto) {
        Message message = Message.of(messageDto);
        messageRepository.save(message);
        return message.getId();
    }
}
