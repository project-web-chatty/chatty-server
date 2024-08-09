package com.messenger.chatty.domain.Message.entity;

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
}
