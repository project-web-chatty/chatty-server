package com.messenger.chatty.service;

import com.messenger.chatty.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceResponseDto;

import java.util.List;

public interface WorkspaceService {
    List<WorkspaceResponseDto> getAllWorkspaceList();
    WorkspaceResponseDto generateWorkspace(
                      WorkspaceGenerateRequestDto workspaceGenerateRequestDto,
                       String username);
    WorkspaceResponseDto getWorkspaceProfile(String workspaceName);
}
