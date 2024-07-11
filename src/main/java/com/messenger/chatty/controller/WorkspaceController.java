package com.messenger.chatty.controller;


import com.messenger.chatty.config.web.AuthenticatedUsername;
import com.messenger.chatty.dto.request.ChannelGenerateRequestDto;
import com.messenger.chatty.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.dto.request.WorkspaceUpdateRequestDto;
import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceResponseDto;
import com.messenger.chatty.service.ChannelService;
import com.messenger.chatty.service.MemberService;
import com.messenger.chatty.service.WorkspaceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/workspace")
@RequiredArgsConstructor
@RestController
public class WorkspaceController {
    private final ChannelService channelService;
    private final WorkspaceService workspaceService;


    // 워크 스페이스 생성
    @PostMapping
    public WorkspaceBriefDto createWorkspace(@RequestParam String username,
                                             @Valid @RequestBody WorkspaceGenerateRequestDto requestDto) {
        return  workspaceService.generateWorkspace(requestDto, username);
    }

    @GetMapping("/{workspaceId}")
    public WorkspaceResponseDto getWorkspaceProfile(@PathVariable Long workspaceId) {
        return workspaceService.getWorkspaceProfile(workspaceId);
    }

    @PutMapping("/{workspaceId}")
    public WorkspaceBriefDto updateWorkspaceProfile
            (@PathVariable Long workspaceId , @RequestBody WorkspaceUpdateRequestDto requestDto
             ) {
        return workspaceService.updateWorkspaceProfile(workspaceId, requestDto);
    }

    @GetMapping("/{workspaceId}/channels")
    public List<ChannelBriefDto> getChannelsOfWorkspace(@PathVariable Long workspaceId ){
        return workspaceService.getChannelsOfWorkspace(workspaceId);
    }

    @PostMapping("/{workspaceId}/channels")
    public ChannelBriefDto addChannelToWorkspace(@PathVariable Long workspaceId,
            @RequestBody @Valid ChannelGenerateRequestDto requestDto ){

        return channelService.createChannelToWorkspace(workspaceId, requestDto);
    }

    // 채널 삭제 api 만들기
  /*  @DeleteMapping("/{workspaceName}/channels/{channelId}")
    public ResponseEntity<ChannelBriefDto> deleteChannelToWorkspace(@PathVariable String workspaceName
           @PathVariable Long channelId  ){

        return ResponseEntity.ok().body(channelService.createChannelToWorkspace(workspaceName, channelName));
    }*/


    @GetMapping("/{workspaceId}/members")
    public List<MemberBriefDto> getMembersOfWorkspace(@PathVariable Long workspaceId ){
        return workspaceService.getMembersOfWorkspace(workspaceId);
    }
    @PostMapping("/{workspaceId}/members")
    public ResponseEntity<Void> enterIntoWorkspace(@PathVariable Long workspaceId,
                                                              @RequestParam String username ){
        workspaceService.enterIntoWorkspace(workspaceId, username);
        return ResponseEntity.ok().build();    }


}
