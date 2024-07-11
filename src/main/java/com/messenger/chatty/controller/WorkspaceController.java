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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "WORKSPACE API", description = "워크스페이스와 관련된 핵심적인 API 들입니다.")
@RequestMapping("/api/workspace")
@RequiredArgsConstructor
@RestController
public class WorkspaceController {
    private final ChannelService channelService;
    private final WorkspaceService workspaceService;


    @Operation(summary = "워크스페이스 생성하기")
    @PostMapping
    public WorkspaceBriefDto createWorkspace(@RequestParam String username,
                                             @Valid @RequestBody WorkspaceGenerateRequestDto requestDto) {
        return  workspaceService.generateWorkspace(requestDto, username);
    }

    @Operation(summary = "워크스페이스 프로필 정보 가져오기")
    @GetMapping("/{workspaceId}")
    public WorkspaceResponseDto getWorkspaceProfile(@PathVariable Long workspaceId) {
        return workspaceService.getWorkspaceProfile(workspaceId);
    }

    @Operation(summary = "워크스페이스 프로필 정보 수정하기")
    @PutMapping("/{workspaceId}")
    public WorkspaceBriefDto updateWorkspaceProfile
            (@PathVariable Long workspaceId , @RequestBody WorkspaceUpdateRequestDto requestDto
             ) {
        return workspaceService.updateWorkspaceProfile(workspaceId, requestDto);
    }

    @Operation(summary = "워크스페이스의 채널 리스트 가져오기")
    @GetMapping("/{workspaceId}/channels")
    public List<ChannelBriefDto> getChannelsOfWorkspace(@PathVariable Long workspaceId ){
        return workspaceService.getChannelsOfWorkspace(workspaceId);
    }

    @Operation(summary = "워크스페이스의 멤버 리스트 가져오기")
    @GetMapping("/{workspaceId}/members")
    public List<MemberBriefDto> getMembersOfWorkspace(@PathVariable Long workspaceId ){
        return workspaceService.getMembersOfWorkspace(workspaceId);
    }

    @Operation(summary = "워크스페이스에 채널 추가하기")
    @PostMapping("/{workspaceId}/channels")
    public ChannelBriefDto addChannelToWorkspace(@PathVariable Long workspaceId,
            @RequestBody @Valid ChannelGenerateRequestDto requestDto ){

        return channelService.createChannelToWorkspace(workspaceId, requestDto);
    }

    @Operation(summary = "특정 채널 삭제하기")
    @DeleteMapping("/channels/{channelId}")
    public ResponseEntity<Void> deleteChannelToWorkspace(
                                                                    @PathVariable Long channelId  ){
        channelService.deleteChannelInWorkspace(channelId);

        return ResponseEntity.ok().build();
    }






}
