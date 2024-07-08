package com.messenger.chatty.service;


import com.messenger.chatty.dto.response.WorkspaceResponseDto;
import com.messenger.chatty.entity.Workspace;
import com.messenger.chatty.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService{

    private final WorkspaceRepository workspaceRepository;

    public List<WorkspaceResponseDto> getAllWorkspaceList() {
        List<Workspace> workspaceList = workspaceRepository.findAll();
        return workspaceList.stream().map(this::convertWorkspaceToDto).toList();
    };



    private WorkspaceResponseDto convertWorkspaceToDto(Workspace workspace){
        return WorkspaceResponseDto.builder().id(workspace.getId())
                .name(workspace.getName())
                .profile_img(workspace.getProfile_img())
                .description(workspace.getDescription())
                .createdDate(workspace.getCreatedDate())
                .lastModifiedDate(workspace.getLastModifiedDate())
                .build();
    }
}
