package com.messenger.chatty.service;

import com.messenger.chatty.dto.response.channel.ChannelBriefDto;

import java.util.List;

public interface ChannelService {
    List<ChannelBriefDto> getChannelsOfWorkspace(String workspaceName);
    ChannelBriefDto createChannelToWorkspace(String targetWorkspaceName, String channelName);

}
