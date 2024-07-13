package com.messenger.chatty.service;

import com.messenger.chatty.dto.request.ChannelGenerateRequestDto;
import com.messenger.chatty.dto.response.channel.ChannelBriefDto;

import java.util.List;

public interface ChannelService {

    ChannelBriefDto createChannelToWorkspace(Long workspaceId, ChannelGenerateRequestDto requestDto);
    List<ChannelBriefDto> getAllChannels();

  //  List<ChannelBriefDto> getChannelsOfMemberAndWorkspace(Long workspaceId, String username);

    void deleteChannelInWorkspace(Long  channelId);

}