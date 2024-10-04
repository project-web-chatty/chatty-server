package com.messenger.chatty.domain.workspace.service;

import com.messenger.chatty.domain.member.dto.response.MemberInWorkspaceDto;
import com.messenger.chatty.domain.workspace.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.domain.workspace.dto.request.WorkspaceUpdateRequestDto;
import com.messenger.chatty.domain.channel.dto.response.ChannelBriefDto;
import com.messenger.chatty.domain.member.dto.response.MemberBriefDto;
import com.messenger.chatty.domain.workspace.dto.response.WorkspaceBriefDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WorkspaceService {
    List<WorkspaceBriefDto> getAllWorkspaceList();

    Long generateWorkspace(
            WorkspaceGenerateRequestDto workspaceGenerateRequestDto,
            String creator);
    WorkspaceBriefDto getWorkspaceProfile(Long workspaceId);

    Long updateWorkspaceProfile(Long workspaceId, WorkspaceUpdateRequestDto requestDto);

    void deleteWorkspace(Long workspaceId);

    WorkspaceBriefDto  getWorkspaceBriefProfile(Long workspaceId);

    List<MemberInWorkspaceDto> getMembersOfWorkspace(Long workspaceId);

    MemberInWorkspaceDto getMemberProfileOfWorkspace(Long workspaceId, Long memberId);

    List<ChannelBriefDto> getChannelsOfWorkspace(Long workspaceId);


    String getInvitationCode(Long workspaceId);
    String setInvitationCode(Long workspaceId);

    void enterToWorkspace(String username, String code);

    void changeRoleOfMember(Long workspaceId,Long memberId,String role);

    String uploadProfileImage(Long workspaceId, MultipartFile file);
    void deleteProfileImage(Long workspaceId);

    void leaveWorkspace(String username,Long workspaceId);

    void deleteMemberFromWorkspace(Long workspaceId, Long memberId);
}
