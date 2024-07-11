package com.messenger.chatty.dto.response.workspace;


import com.messenger.chatty.dto.response.BaseResDto;
import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Getter
@Setter
@SuperBuilder
@ToString
public class WorkspaceResponseDto extends WorkspaceBriefDto {
    private List<MemberBriefDto> members;
    private List<ChannelBriefDto> channels;
}
