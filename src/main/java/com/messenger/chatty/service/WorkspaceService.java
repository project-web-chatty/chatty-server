package com.messenger.chatty.service;

import com.messenger.chatty.dto.response.WorkspaceResponseDto;
import com.messenger.chatty.entity.Workspace;

import java.util.List;

public interface WorkspaceService {
    List<WorkspaceResponseDto> getAllWorkspaceList();
}
