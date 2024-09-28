package com.messenger.chatty.domain.message.controller;

import com.messenger.chatty.domain.message.dto.request.MessageDto;
import com.messenger.chatty.domain.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final RabbitTemplate template;
    private final MessageService messageService;

    @MessageMapping("chat.enter")
    public void enter(MessageDto messageDto, @Header("channelId") String channelIdStr) {
        log.info("enter");
        if(channelIdStr == null) throw new MessagingException("error");     //TODO unify form
        messageDto.setContent("입장하셨습니다.");
        messageDto.setRegDate(LocalDateTime.now());
        //TODO 입장 시 messageDto setting

        messageService.send(messageDto);
        template.convertAndSend("amq.topic", "channel." + channelIdStr, messageDto); //topic
    }


    @MessageMapping("chat.message")
    public void send(MessageDto messageDto, @Header("channelId") String channelIdStr) {
        log.info("send");
        if(channelIdStr == null) throw new MessagingException("error");
        messageDto.setRegDate(LocalDateTime.now());

        messageService.send(messageDto);
        template.convertAndSend("amq.topic", "channel." + channelIdStr, messageDto);
    }


    // receiver()는 단순히 큐에 들어온 메세지를 소비만 한다. (현재는 디버그 용도)
   /* @RabbitListener(queues = "message.queue")
    public void receive(MessageDto messageDto) {
        log.info("chatDto.getMessage() = {}", messageDto.getContent());
    }*/

}
