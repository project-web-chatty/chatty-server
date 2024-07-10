package com.messenger.chatty.service;


import com.messenger.chatty.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.dto.response.WorkspaceResponseDto;
import com.messenger.chatty.entity.Channel;
import com.messenger.chatty.entity.Member;
import com.messenger.chatty.entity.Workspace;
import com.messenger.chatty.exception.custom.DuplicatedNameException;
import com.messenger.chatty.repository.ChannelRepository;
import com.messenger.chatty.repository.MemberRepository;
import com.messenger.chatty.repository.WorkspaceRepository;
import com.messenger.chatty.util.DtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService{

    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ChannelRepository channelRepository;

    public List<WorkspaceResponseDto> getAllWorkspaceList() {
        List<Workspace> workspaceList = workspaceRepository.findAll();
        return workspaceList.stream().map(this::convertWorkspaceToDto).toList();
    };

    @Override
    public WorkspaceResponseDto generateWorkspace(WorkspaceGenerateRequestDto generateRequestDto, String username) {
        if(workspaceRepository.existsByName(generateRequestDto.getName()))
            throw new DuplicatedNameException("duplicated workspaceName : "+ generateRequestDto.getName());

        // 멤버
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("there is no member whose username is " + username));
        Workspace workspace = Workspace.generateWorkspace(generateRequestDto);
        workspaceRepository.save(workspace); // 워크스페이스 저장

        member.enterIntoWorkspace(workspace);
        // 채널 등록
        Channel defaultChannel = Channel.createChannel("announce",workspace);
        channelRepository.save(defaultChannel);
        member.enterIntoChannel(defaultChannel);
        return DtoConverter.convertWorkspaceToDto(workspace);
    }

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
