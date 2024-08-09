package com.messenger.chatty.domain.Message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
