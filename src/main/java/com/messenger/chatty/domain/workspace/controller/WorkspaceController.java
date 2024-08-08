package com.messenger.chatty.domain.workspace.controller;
import com.messenger.chatty.domain.workspace.dto.response.WorkspaceBriefDto;
import com.messenger.chatty.global.config.web.AuthenticatedUsername;
import com.messenger.chatty.domain.channel.dto.request.ChannelGenerateRequestDto;
import com.messenger.chatty.domain.workspace.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.domain.workspace.dto.request.WorkspaceUpdateRequestDto;
import com.messenger.chatty.domain.channel.dto.response.ChannelBriefDto;
import com.messenger.chatty.domain.member.dto.response.MemberBriefDto;
import com.messenger.chatty.global.presentation.ApiResponse;
import com.messenger.chatty.domain.channel.service.ChannelService;
import com.messenger.chatty.domain.workspace.service.WorkspaceService;
import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.annotation.api.ApiErrorCodeExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @ApiErrorCodeExample({
            ErrorStatus.WORKSPACE_NAME_ALREADY_EXISTS,
            ErrorStatus.WORKSPACE_NOT_FOUND,
            ErrorStatus._UNINTENDED_AUTHENTICATION_ERROR
    })
    @PostMapping
    public ApiResponse<Long> createWorkspace(@AuthenticatedUsername String username,
                                             @Valid @RequestBody WorkspaceGenerateRequestDto requestDto) {
        return ApiResponse.onSuccess(workspaceService.generateWorkspace(requestDto, username));
    }

    @Operation(summary = "워크스페이스 프로필 정보 가져오기")
    @ApiErrorCodeExample({
            ErrorStatus.WORKSPACE_NOT_FOUND
    })
    @GetMapping("/{workspaceId}")
    public ApiResponse<WorkspaceBriefDto> getWorkspaceProfile(@PathVariable Long workspaceId) {
        return ApiResponse.onSuccess(workspaceService.getWorkspaceProfile(workspaceId));
    }

    @Operation(summary = "워크스페이스 프로필 정보 수정하기")
    @ApiErrorCodeExample({
            ErrorStatus.WORKSPACE_NOT_FOUND
    })
    @PutMapping("/{workspaceId}")
    public ApiResponse<Long> updateWorkspaceProfile(@PathVariable Long workspaceId,
                                                    @RequestBody WorkspaceUpdateRequestDto requestDto) {
        return ApiResponse.onSuccess(workspaceService.updateWorkspaceProfile(workspaceId, requestDto));
    }

    @Operation(summary = "워크스페이스의 채널 리스트 가져오기")
    @ApiErrorCodeExample({
            ErrorStatus.WORKSPACE_NOT_FOUND
    })
    @GetMapping("/{workspaceId}/channels")
    public ApiResponse<List<ChannelBriefDto>> getChannelsOfWorkspace(@PathVariable Long workspaceId){
        return ApiResponse.onSuccess(workspaceService.getChannelsOfWorkspace(workspaceId));
    }

    @Operation(summary = "워크스페이스의 멤버 리스트 가져오기")
    @ApiErrorCodeExample({
            ErrorStatus.WORKSPACE_NOT_FOUND
    })
    @GetMapping("/{workspaceId}/members")
    public ApiResponse<List<MemberBriefDto>> getMembersOfWorkspace(@PathVariable Long workspaceId){
        return ApiResponse.onSuccess(workspaceService.getMembersOfWorkspace(workspaceId));
    }

    @Operation(summary = "해당 워크스페이스 내에서의 특정 멤버 데이터를 가져오기(기존 멤버 데이터 get 요청과는 role 필드만 달라집니다.)")
    @ApiErrorCodeExample({
            ErrorStatus.MEMBER_NOT_FOUND,
            ErrorStatus.MEMBER_NOT_IN_WORKSPACE
    })
    @GetMapping("/{workspaceId}/members/{memberId}")
    public ApiResponse<MemberBriefDto> getMemberProfileOfWorkspace(@PathVariable Long workspaceId,
                                                                   @PathVariable Long memberId){
        return ApiResponse.onSuccess(workspaceService.getMemberProfileOfWorkspace(workspaceId, memberId));
    }

    @Operation(summary = "워크스페이스에 채널 추가하기")
    @ApiErrorCodeExample({
            ErrorStatus.WORKSPACE_NOT_FOUND,
            ErrorStatus.CHANNEL_NAME_ALREADY_EXISTS
    })
    @PostMapping("/{workspaceId}/channels")
    public ApiResponse<Long> addChannelToWorkspace(@PathVariable Long workspaceId,
                                                   @RequestBody @Valid ChannelGenerateRequestDto requestDto) {
        return ApiResponse.onSuccess(channelService.createChannelToWorkspace(workspaceId, requestDto));
    }


    @Operation(summary = "해당 워크스페이스의 초대링크 가져오기")
    @ApiErrorCodeExample({
            ErrorStatus.WORKSPACE_NOT_FOUND
    })
    @GetMapping("/{workspaceId}/invite")
    public ApiResponse<String> getNewInvitationCode(@PathVariable  Long workspaceId){
        return ApiResponse.onSuccess(workspaceService.getInvitationCode(workspaceId));
    }

    @Operation(summary = "해당 워크스페이스의 초대링크 갱신하기", description = "워크 스페이스의 초대링크를 새로 갱신할때 사용합니다")
    @ApiErrorCodeExample({
            ErrorStatus.WORKSPACE_NOT_FOUND
    })
    @PostMapping("/{workspaceId}/invite")
    public ApiResponse<String> generateInvitationCode(@PathVariable Long workspaceId) {
        return ApiResponse.onSuccess(workspaceService.setInvitationCode(workspaceId));
    }


    @Operation(summary = "특정 채널 삭제하기")
    @ApiErrorCodeExample({
            ErrorStatus.CHANNEL_NOT_FOUND,
            ErrorStatus.CHANNEL_NOT_IN_WORKSPACE
    })
    @DeleteMapping("/{workspaceId}/channels/{channelId}")
    public ApiResponse<Boolean> deleteChannelToWorkspace(@PathVariable Long workspaceId,
                                                         @PathVariable Long channelId) {
        channelService.deleteChannelInWorkspace(workspaceId, channelId);
        return ApiResponse.onSuccess(true);
    }


    @Operation(summary = "워크스페이스 내 특정 멤버의 ROLE을 바꾸기")
    @ApiErrorCodeExample({
            ErrorStatus.WORKSPACE_INVALID_ROLE_CHANGE_REQUEST,
            ErrorStatus.MEMBER_NOT_IN_WORKSPACE
    })
    @PutMapping("/{workspaceId}/role/{memberId}")
    public ApiResponse<Boolean> changeRoleOfMember(@PathVariable Long workspaceId,
                                                   @PathVariable Long memberId,
                                                   @RequestParam String role){
        workspaceService.changeRoleOfMember(workspaceId,memberId,role);
        return ApiResponse.onSuccess(true);
    }


    @Operation(summary = "워크 스페이스에 참여",
            description = "초대링크를 브라우저창에 입력해서 웹서버 측에 페이지를 요청했을때, " +
                    "웹서버 측은 이 엔드포인트 단으로 곧바로 요청을 보낸다. 앞 필터에서 먼저 리프레시 토큰으로 로그인 여부를 확인하고 " +
                    "로그인 되어 있다면 이후 로직을 시행, 로그인 되어 있지 않으면 로그인페이지로 리다이렉팅 시킨다.")
    @ApiErrorCodeExample({
            ErrorStatus.WORKSPACE_INVALID_INVITATION_CODE,
            ErrorStatus.MEMBER_NOT_FOUND,
            ErrorStatus.MEMBER_ALREADY_EXISTS_IN_WORKSPACE
    })
    // 로그인 되었다고 가정
    @PostMapping("/join/{code}")
    public ApiResponse<Boolean> joinToWorkspace(@PathVariable String code,
                                                @AuthenticatedUsername String username) {
        workspaceService.enterToWorkspace(username, code);
        return ApiResponse.onSuccess(true);
    }
}
