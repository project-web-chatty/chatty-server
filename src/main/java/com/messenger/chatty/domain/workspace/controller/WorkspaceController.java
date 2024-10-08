package com.messenger.chatty.domain.workspace.controller;
import com.messenger.chatty.domain.message.service.MessageService;
import com.messenger.chatty.domain.member.dto.response.MemberInWorkspaceDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.messenger.chatty.global.presentation.ErrorStatus.*;
import static com.messenger.chatty.global.presentation.ErrorStatus.IO_EXCEPTION_ON_IMAGE_DELETE;

@Slf4j
@Tag(name = "WORKSPACE API", description = "워크스페이스와 관련된 핵심적인 API 들입니다.")
@RequestMapping("/api/workspace")
@RequiredArgsConstructor
@RestController
public class WorkspaceController {
    private final ChannelService channelService;
    private final WorkspaceService workspaceService;
    private final MessageService messageService;


    @Operation(summary = "워크스페이스 생성하기")
    @ApiErrorCodeExample({
            ErrorStatus.WORKSPACE_NAME_ALREADY_EXISTS,
            ErrorStatus.WORKSPACE_NOT_FOUND,
            ErrorStatus._UNINTENDED_AUTHENTICATION_ERROR
    })
    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Long> createWorkspace(@AuthenticatedUsername String username,
                                             @Valid @ModelAttribute WorkspaceGenerateRequestDto requestDto) {
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

    @Operation(summary = "워크스페이스 프로필 정보 수정하기",description = "워크스페이스 오너 이상의 역할만 가능합니다")
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
    public ApiResponse<List<ChannelBriefDto>> getChannelsOfWorkspace(@AuthenticatedUsername String username,
                                                                     @PathVariable Long workspaceId){
        List<ChannelBriefDto> channelBriefDtoList = workspaceService.getChannelsOfWorkspace(workspaceId);
        channelBriefDtoList.forEach(dto ->
            dto.setUnreadCount(
                    messageService.countUnreadMessage(dto.getId(), channelService.getWorkspaceJoinId(dto.getId(), username))
            )
        );
        return ApiResponse.onSuccess(channelBriefDtoList);
    }

    @Operation(summary = "워크스페이스의 멤버 리스트 가져오기")
    @ApiErrorCodeExample({
            ErrorStatus.WORKSPACE_NOT_FOUND
    })
    @GetMapping("/{workspaceId}/members")
    public ApiResponse<List<MemberInWorkspaceDto>> getMembersOfWorkspace(@PathVariable Long workspaceId){
        return ApiResponse.onSuccess(workspaceService.getMembersOfWorkspace(workspaceId));
    }

    @Operation(summary = "해당 워크스페이스 내에서의 특정 멤버 데이터를 가져오기",description = "이 요청의 응답에서는 role field가 워크스페이스 내 역할 값을 가집니다." +
            "워크스페이스 내 역할은 일반 멤버(ROLE_WORKSPACE_MEMBER이거나 워크스페이스의 오너(ROLE_WORKSPACE_OWNER)입니다. 오너는 워크스페이스 정보 수정, 채널 추가, 삭제 등의 권한을 가집니다.")
    @ApiErrorCodeExample({
            ErrorStatus.MEMBER_NOT_FOUND,
            ErrorStatus.MEMBER_NOT_IN_WORKSPACE
    })
    @GetMapping("/{workspaceId}/members/{memberId}")
    public ApiResponse<MemberInWorkspaceDto> getMemberProfileOfWorkspace(@PathVariable Long workspaceId,
                                                                   @PathVariable Long memberId){
        return ApiResponse.onSuccess(workspaceService.getMemberProfileOfWorkspace(workspaceId, memberId));
    }


    @Operation(summary = "워크스페이스에서 특정 멤버를 내보내기",description = "특정 멤버를 워크스페이스에서 삭제시킵니다.")
    @ApiErrorCodeExample({
            ErrorStatus.MEMBER_NOT_IN_WORKSPACE
    })
    @DeleteMapping("/{workspaceId}/members/{memberId}")
    public ApiResponse<Boolean> deleteMemberFromWorkspace(@PathVariable Long workspaceId,
                                                                   @PathVariable Long memberId){
        workspaceService.deleteMemberFromWorkspace(workspaceId,memberId);
        return ApiResponse.onSuccess(true);
    }


    @Operation(summary = "워크스페이스에 채널 추가하기",description = "워크스페이스 오너 이상의 역할만 가능합니다")
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

    @Operation(summary = "해당 워크스페이스의 초대링크 갱신하기", description = "워크 스페이스의 초대링크를 새로 갱신할때 사용합니다. 워크스페이스 오너 이상의 역할만 가능합니다.")
    @ApiErrorCodeExample({
            ErrorStatus.WORKSPACE_NOT_FOUND
    })
    @PostMapping("/{workspaceId}/invite")
    public ApiResponse<String> generateInvitationCode(@PathVariable Long workspaceId) {
        return ApiResponse.onSuccess(workspaceService.setInvitationCode(workspaceId));
    }


    @Operation(summary = "특정 채널 삭제하기",description = "워크스페이스 오너 이상의 역할만 가능합니다.")
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


    @Operation(summary = "워크스페이스 내 특정 멤버의 ROLE을 바꾸기",description = "워크스페이스 오너 이상의 역할만 가능합니다")
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


    @Operation(summary = "특정 워크스페이스 삭제하기",description = "워크스페이스 오너 이상의 역할만 가능합니다.")
    @ApiErrorCodeExample({
            ErrorStatus.WORKSPACE_NOT_FOUND
    })
    @DeleteMapping("/{workspaceId}")
    public ApiResponse<Boolean> deleteChannelToWorkspace(@PathVariable Long workspaceId) {
        workspaceService.deleteWorkspace(workspaceId);
        return ApiResponse.onSuccess(true);
    }



    @Operation(summary = "워크스페이스 프로필 이미지 업로드하기(수정 포함)")
    @ApiErrorCodeExample({
            WORKSPACE_NOT_FOUND,
            EMPTY_FILE_EXCEPTION,
            NO_FILE_EXTENSION,
            INVALID_FILE_EXTENSION,
            INVALID_FILE_URI,
            IO_EXCEPTION_ON_IMAGE_UPLOAD,
            IO_EXCEPTION_ON_IMAGE_DELETE
    })
    @PostMapping(value= "/{workspaceId}/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> uploadWorkspaceProfileImg(@PathVariable Long workspaceId,
                                                         @RequestParam("file") MultipartFile file) {
        String profileImageURI = workspaceService.uploadProfileImage(workspaceId, file);
        return ApiResponse.onSuccess(profileImageURI);
    }


    @Operation(summary = "워크스페이스 프로필 이미지 삭제하기",description = "워크스페이스의 프로필 이미지가 존재하는 경우 삭제합니다.")
    @ApiErrorCodeExample({
            WORKSPACE_NOT_FOUND,
            INVALID_FILE_URI,
            IO_EXCEPTION_ON_IMAGE_DELETE
    })
    @DeleteMapping("/{workspaceId}/profile-image")
    public ApiResponse<Boolean> deleteWorkspaceProfileImg(@PathVariable Long workspaceId){
        workspaceService.deleteProfileImage(workspaceId);
        return ApiResponse.onSuccess(true);
    }





}
