package com.messenger.chatty.domain.member.controller;
import com.messenger.chatty.domain.channel.dto.response.ChannelBriefDto;
import com.messenger.chatty.domain.member.dto.response.MemberBriefDto;
import com.messenger.chatty.domain.workspace.dto.response.WorkspaceBriefDto;
import com.messenger.chatty.global.presentation.ApiResponse;
import com.messenger.chatty.domain.channel.service.ChannelService;
import com.messenger.chatty.domain.member.service.MemberService;
import com.messenger.chatty.domain.workspace.service.WorkspaceService;
import com.messenger.chatty.global.presentation.annotation.api.ApiErrorCodeExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ADMIN API", description = "ADMIN 전용 API입니다.")
@RequestMapping("/api/admin") // for the admin of this web service
@RequiredArgsConstructor
@RestController
public class AdminController {
    private final MemberService memberService;
    private final WorkspaceService workspaceService;
    private final ChannelService channelService;


    @Operation(summary = "모든 멤버 리스트 가져오기")
    @ApiErrorCodeExample
    @GetMapping("/members")
    public ApiResponse<List<MemberBriefDto>> getAllMemberList(){
        return ApiResponse.onSuccess(memberService.getAllMemberList());
    }

    @Operation(summary = "모든 워크스페이스 리스트 가져오기")
    @ApiErrorCodeExample
    @GetMapping("/workspaces")
    public ApiResponse<List<WorkspaceBriefDto>> getAllWorkspaceList(){
        return ApiResponse.onSuccess(workspaceService.getAllWorkspaceList());
    }

    @Operation(summary = "모든 채널 가져오기")
    @ApiErrorCodeExample
    @GetMapping("/channels")
    public ApiResponse<List<ChannelBriefDto>> getAllChannelsList(){
        return ApiResponse.onSuccess(channelService.getAllChannels());
    }
}
