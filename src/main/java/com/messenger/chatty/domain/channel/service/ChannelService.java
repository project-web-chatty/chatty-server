package com.messenger.chatty.domain.channel.service;

import com.messenger.chatty.domain.channel.dto.request.ChannelGenerateRequestDto;
import com.messenger.chatty.domain.channel.dto.response.ChannelBriefDto;
import com.messenger.chatty.domain.member.dto.response.MemberBriefDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ChannelService {

    Long createChannelToWorkspace(Long workspaceId, ChannelGenerateRequestDto requestDto);
    List<ChannelBriefDto> getAllChannels();

  //  List<ChannelBriefDto> getChannelsOfMemberAndWorkspace(Long workspaceId, String username);

    void deleteChannelInWorkspace(Long workspaceId, Long  channelId);

    Long getWorkspaceJoinId(Long channelId, String username);

    boolean validateEnterChannel(Long channelId, String username);

    void updateAccessTime(Long channelId, Long workspaceJoinId, LocalDateTime currentTime);

    Long createAccessTime(Long channelId, Long workspaceJoinId);

    boolean hasAccessTime(Long channelId, Long workspaceJoinId);

    String getUnreadMessageId(Long channelId, Long workspaceJoinId);

    MemberBriefDto getMemberInfoByWorkspace(Long workspaceJoinId);
}
