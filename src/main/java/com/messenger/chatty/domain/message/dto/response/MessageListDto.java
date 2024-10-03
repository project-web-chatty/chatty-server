package com.messenger.chatty.domain.message.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MessageListDto {
    @Builder.Default
    private List<MessageResponseDto> messageResponseDtoList = new ArrayList<>();
    private boolean isLast;
    private boolean isHavePoint;
}
