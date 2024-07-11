package com.messenger.chatty.service;

import com.messenger.chatty.dto.response.workspace.WorkspaceResponseDto;

public interface InviteService {
    String getNewInvitationCode(Long workspaceId);
    String setInvitationCode(Long workspaceId);

    WorkspaceResponseDto acceptInvitationAndEnterToWorkspace(String username, String code);



}
