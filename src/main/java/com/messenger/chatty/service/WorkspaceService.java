package com.messenger.chatty.service;

import com.messenger.chatty.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.dto.request.WorkspaceUpdateRequestDto;
import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceResponseDto;

import java.util.List;

public interface WorkspaceService {
    List<WorkspaceBriefDto> getAllWorkspaceList();
    WorkspaceBriefDto generateWorkspace(
                      WorkspaceGenerateRequestDto workspaceGenerateRequestDto,
                       String creator);
    WorkspaceResponseDto getWorkspaceProfile(Long workspaceId);

    WorkspaceBriefDto updateWorkspaceProfile(Long workspaceId, WorkspaceUpdateRequestDto requestDto);

    void deleteWorkspace(Long workspaceId);

    WorkspaceBriefDto  getWorkspaceBriefProfile(Long workspaceId);

    List<MemberBriefDto> getMembersOfWorkspace(Long workspaceId);

    List<ChannelBriefDto> getChannelsOfWorkspace(Long workspaceId);

    void enterIntoWorkspace(Long workspaceId, String targetUsername);




}
