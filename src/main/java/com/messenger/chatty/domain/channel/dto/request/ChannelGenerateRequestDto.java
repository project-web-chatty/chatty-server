package com.messenger.chatty.domain.channel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class ChannelGenerateRequestDto {
    @NotBlank(message = "채널 이름은 필수 입력사항입니다.")
    @Size(min = 1, max = 20, message = "채널 이름은 최소 1자에서 최대 20자까지 가능합니다.")
    private String name;
}
