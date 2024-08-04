package com.messenger.chatty.domain.channel.dto.response;

import com.messenger.chatty.domain.base.dto.response.BaseResDto;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@ToString
public class ChannelBriefDto extends BaseResDto {

    private Long id;
    private String name;

}
