package com.messenger.chatty.domain.Message.service;

import com.messenger.chatty.domain.Message.dto.MessageDto;

public interface MessageService {

    public String send(MessageDto messageDto);
}
