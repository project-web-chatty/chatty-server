package com.messenger.chatty.service;

import com.messenger.chatty.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.dto.response.WorkspaceResponseDto;
import com.messenger.chatty.entity.Workspace;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface WorkspaceService {
    List<WorkspaceResponseDto> getAllWorkspaceList();
    WorkspaceResponseDto generateWorkspace(
                      WorkspaceGenerateRequestDto workspaceGenerateRequestDto,
                       String username);
}
