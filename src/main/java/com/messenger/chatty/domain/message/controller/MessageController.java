package com.messenger.chatty.domain.message.controller;

import com.messenger.chatty.domain.message.dto.request.MessageDto;
import com.messenger.chatty.domain.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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
    public void send(MessageDto messageDto, SimpMessageHeaderAccessor headerAccessor) {
        log.info("Received Headers: {}", headerAccessor.getSessionAttributes()); // 전체 헤더 로그 출력

        // SimpMessageHeaderAccessor를 사용하여 세션에서 속성 값 가져오기
        String nickname = (String) headerAccessor.getSessionAttributes().get("nickname");
        String profileImg = (String) headerAccessor.getSessionAttributes().get("profileImg");
        Long workspaceJoinId = (Long) headerAccessor.getSessionAttributes().get("workspaceJoinId");
        Long channelId = (Long) headerAccessor.getSessionAttributes().get("channelId");

        log.info("channelId : {}, workspaceJoinId : {}, nickname : {}, profileImg : {}", channelId, workspaceJoinId, nickname, profileImg);
        messageDto.setRegDate(LocalDateTime.now());
        messageDto.of(workspaceJoinId, profileImg, nickname);

        messageService.send(messageDto);
        template.convertAndSend("amq.topic", "channel." + channelId, messageDto);
    }


    // receiver()는 단순히 큐에 들어온 메세지를 소비만 한다. (현재는 디버그 용도)
   /* @RabbitListener(queues = "message.queue")
    public void receive(MessageDto messageDto) {
        log.info("chatDto.getMessage() = {}", messageDto.getContent());
    }*/

}
