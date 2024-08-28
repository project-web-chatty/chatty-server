package com.messenger.chatty.domain.channel.service;

import com.messenger.chatty.domain.channel.dto.request.ChannelGenerateRequestDto;
import com.messenger.chatty.domain.channel.dto.response.ChannelBriefDto;

import java.util.List;

public interface ChannelService {

    Long createChannelToWorkspace(Long workspaceId, ChannelGenerateRequestDto requestDto);
    List<ChannelBriefDto> getAllChannels();

  //  List<ChannelBriefDto> getChannelsOfMemberAndWorkspace(Long workspaceId, String username);

    void deleteChannelInWorkspace(Long workspaceId, Long  channelId);

    boolean validateEnterChannel(Long channelId, String username);

    void updateAccessTime(Long channelId, String username);

    Long createAccessTime(Long channelId, String username);

    boolean hasAccessTime(Long channelId, String username);


}
