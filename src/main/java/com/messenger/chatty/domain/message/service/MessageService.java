package com.messenger.chatty.domain.message.service;

import com.messenger.chatty.domain.message.dto.MessageDto;

public interface MessageService {

    public String send(MessageDto messageDto);
}
