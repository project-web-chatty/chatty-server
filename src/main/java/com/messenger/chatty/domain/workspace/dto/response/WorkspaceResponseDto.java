package com.messenger.chatty.domain.workspace.dto.response;


import com.messenger.chatty.domain.channel.dto.response.ChannelBriefDto;
import com.messenger.chatty.domain.member.dto.response.MemberBriefDto;
import com.messenger.chatty.domain.workspace.dto.response.WorkspaceBriefDto;
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
