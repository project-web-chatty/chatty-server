package com.messenger.chatty.service;

import com.messenger.chatty.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceResponseDto;

import java.util.List;

public interface WorkspaceService {
    List<WorkspaceBriefDto> getAllWorkspaceList();
    WorkspaceBriefDto generateWorkspace(
                      WorkspaceGenerateRequestDto workspaceGenerateRequestDto,
                       String username);
    WorkspaceResponseDto getWorkspaceProfile(String workspaceName);

    WorkspaceBriefDto updateWorkspaceProfile(String targetWorkspaceName,String profile_img,String description);

    void deleteWorkspaceProfile(String targetWorkspaceName);

    WorkspaceBriefDto  getWorkspaceBriefProfile(String workspaceName);

    List<MemberBriefDto> getMembersOfWorkspace(String workspaceName);

    void enterIntoWorkspace(String workspaceName, String targetUsername);


}
