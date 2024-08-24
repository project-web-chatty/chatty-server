package com.messenger.chatty.domain.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String id;
    private Long channelId;
    private String senderNickname;
    private String senderUsername;
    private String content;
    private LocalDateTime regDate;

}
