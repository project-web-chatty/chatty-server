package com.messenger.chatty.domain.Message.controller;

import com.messenger.chatty.domain.Message.dto.MessageDto;
import com.messenger.chatty.domain.Message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final RabbitTemplate template;
    private final MessageService messageService;

    @MessageMapping("chat.enter.{channelId}")
    public void enter(MessageDto messageDto, @DestinationVariable String channelId) {
        messageDto.setContent("입장하셨습니다.");
        messageDto.setRegDate(LocalDateTime.now());
        //TODO 입장 시 messageDto setting

        messageService.send(messageDto);
        template.convertAndSend("amq.topic", "channel." + channelId, messageDto); //topic
    }


    @MessageMapping("chat.message.{channelId}")
    public void send(MessageDto messageDto, @DestinationVariable String channelId) {
        messageDto.setRegDate(LocalDateTime.now());

        messageService.send(messageDto);
        template.convertAndSend("amq.topic", "channel." + channelId, messageDto);
    }

    // receiver()는 단순히 큐에 들어온 메세지를 소비만 한다. (현재는 디버그 용도)
    @RabbitListener(queues = "message.queue")
    public void receive(MessageDto messageDto) {
        log.info("chatDto.getMessage() = {}", messageDto.getContent());
    }
}
