package com.messenger.chatty.dto.response.channel;

import com.messenger.chatty.dto.response.BaseResDto;
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
