package com.messenger.chatty.service;

import com.messenger.chatty.dto.response.channel.ChannelBriefDto;

import java.util.List;

public interface ChannelService {

    ChannelBriefDto createChannelToWorkspace(String targetWorkspaceName, String channelName);

    List<ChannelBriefDto> getChannelsOfMemberAndWorkspace(String workspaceName, String username);

}
