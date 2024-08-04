package com.messenger.chatty.domain.channel.service;

import com.messenger.chatty.domain.channel.dto.request.ChannelGenerateRequestDto;
import com.messenger.chatty.domain.channel.dto.response.ChannelBriefDto;

import java.util.List;

public interface ChannelService {

    Long createChannelToWorkspace(Long workspaceId, ChannelGenerateRequestDto requestDto);
    List<ChannelBriefDto> getAllChannels();

  //  List<ChannelBriefDto> getChannelsOfMemberAndWorkspace(Long workspaceId, String username);

    void deleteChannelInWorkspace(Long workspaceId, Long  channelId);


}