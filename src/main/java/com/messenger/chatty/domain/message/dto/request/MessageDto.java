package com.messenger.chatty.domain.message.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long channelId;
    private Long workspaceJoinId;
    private String senderProfileImg;
    private String senderNickname;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime regDate;

    public void of(Long workspaceJoinId, String senderProfileImg, String senderNickname) {
        this.workspaceJoinId = workspaceJoinId;
        this.senderNickname = senderNickname;
        this.senderProfileImg = senderProfileImg;
    }
}
