package com.messenger.chatty.domain.message.entity;

import com.messenger.chatty.domain.message.dto.request.MessageDto;
import com.messenger.chatty.global.util.TimeUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@Document(collection = "message")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    private String id;

    @NotNull
    private Long channelId;

    @NotBlank
    private String content;

    @NotBlank
    private String senderNickname;
    @NotBlank
    private String senderUsername;
    @NotBlank
    private String senderProfileImg;

    @NotNull
    private Long sendTime;

    public static Message of(MessageDto messageDto) {
        return Message.builder()
                .channelId(messageDto.getChannelId())
                .content(messageDto.getContent())
                .senderNickname(messageDto.getSenderNickname())
                .senderUsername(messageDto.getSenderUsername())
                .senderProfileImg(messageDto.getSenderProfileImg())
                .sendTime(TimeUtil.convertTimeTypeToLong(messageDto.getRegDate()))
                .build();
    }
}
