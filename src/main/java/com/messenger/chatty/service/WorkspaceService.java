package com.messenger.chatty.service;

import com.messenger.chatty.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceResponseDto;

import java.util.List;

public interface WorkspaceService {
    List<WorkspaceBriefDto> getAllWorkspaceList();
    WorkspaceBriefDto generateWorkspace(
                      WorkspaceGenerateRequestDto workspaceGenerateRequestDto,
                       String username);
    WorkspaceResponseDto getWorkspaceProfile(String workspaceName);

    WorkspaceBriefDto  getWorkspaceBriefProfile(String workspaceName);


}
