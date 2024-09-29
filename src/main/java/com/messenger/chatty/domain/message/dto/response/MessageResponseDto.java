package com.messenger.chatty.domain.message.dto.response;

import com.messenger.chatty.domain.message.dto.request.MessageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto extends MessageDto {
    private String id;
}
