package com.messenger.chatty.controller;
import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import com.messenger.chatty.presentation.ApiResponse;
import com.messenger.chatty.service.ChannelService;
import com.messenger.chatty.service.MemberService;
import com.messenger.chatty.service.WorkspaceService;
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
    @GetMapping("/members")
    public ApiResponse<List<MemberBriefDto>> getAllMemberList(){

        return ApiResponse.ok(memberService.getAllMemberList());


    }

    @Operation(summary = "모든 워크스페이스 리스트 가져오기")
    @GetMapping("/workspaces")
    public ApiResponse<List<WorkspaceBriefDto>> getAllWorkspaceList(){

        return ApiResponse.ok(workspaceService.getAllWorkspaceList());

    }

    @Operation(summary = "모든 채널 가져오기")
    @GetMapping("/channels")
    public ApiResponse<List<ChannelBriefDto>> getAllChannelsList()
    {
        return ApiResponse.ok(channelService.getAllChannels());
    }




}
