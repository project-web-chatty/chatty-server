package com.messenger.chatty.domain.channel.dto.response;

import com.messenger.chatty.domain.base.dto.response.BaseResDto;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ChannelBriefDto extends BaseResDto {

    private Long id;
    private String name;
    private Long unreadCount;

}
