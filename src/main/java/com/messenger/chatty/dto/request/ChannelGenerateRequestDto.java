package com.messenger.chatty.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChannelGenerateRequestDto {
    @NotBlank
    private String name;
}
