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

    Long generateWorkspace(
            WorkspaceGenerateRequestDto workspaceGenerateRequestDto,
            String creator);
    WorkspaceResponseDto getWorkspaceProfile(Long workspaceId);

    Long updateWorkspaceProfile(Long workspaceId, WorkspaceUpdateRequestDto requestDto);

    void deleteWorkspace(Long workspaceId);

    WorkspaceBriefDto  getWorkspaceBriefProfile(Long workspaceId);

    List<MemberBriefDto> getMembersOfWorkspace(Long workspaceId);

    List<ChannelBriefDto> getChannelsOfWorkspace(Long workspaceId);


    String getInvitationCode(Long workspaceId);
    String setInvitationCode(Long workspaceId);

    void enterToWorkspace(String username, String code);

    void changeRoleOfMember(Long workspaceId,Long memberId,String role);

}
