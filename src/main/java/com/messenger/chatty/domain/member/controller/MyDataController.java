package com.messenger.chatty.domain.member.controller;
import com.messenger.chatty.domain.workspace.dto.response.MyWorkspaceDto;
import com.messenger.chatty.domain.workspace.entity.Workspace;
import com.messenger.chatty.domain.workspace.service.WorkspaceService;
import com.messenger.chatty.global.config.web.AuthenticatedUsername;
import com.messenger.chatty.domain.member.dto.request.MemberUpdateRequestDto;
import com.messenger.chatty.domain.member.dto.response.MyProfileDto;
import com.messenger.chatty.domain.workspace.dto.response.WorkspaceBriefDto;
import com.messenger.chatty.global.presentation.ApiResponse;
import com.messenger.chatty.domain.member.service.MemberService;
import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.annotation.api.ApiErrorCodeExample;
import com.messenger.chatty.global.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.messenger.chatty.global.presentation.ErrorStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Tag(name = "MY DATA API", description = "유저 자신의 리소스 접근과 관련된 API입니다.")
@RequestMapping("/api/me")
@RequiredArgsConstructor
@RestController
public class MyDataController {
    private final MemberService memberService;
    private final WorkspaceService workspaceService;

    @Operation(summary = "내 프로필 정보 가져오기")
    @ApiErrorCodeExample({
            ErrorStatus.MEMBER_NOT_FOUND
    })
    @GetMapping
    public ApiResponse<MyProfileDto> getMyProfile(@AuthenticatedUsername String username) {
        return ApiResponse.onSuccess(memberService.getMyProfileByUsername(username));
    }

    @Operation(summary = "내 프로필 정보 수정하기")
    @ApiErrorCodeExample({
            ErrorStatus.MEMBER_NOT_FOUND
    })
    @PutMapping
    public ApiResponse<Long> changeMyProfile(@AuthenticatedUsername String username,
                                             @RequestBody @Valid MemberUpdateRequestDto updateRequestDto) {
        return ApiResponse.onSuccess(memberService.updateMyProfile(username, updateRequestDto));
    }


    @Operation(summary = "내 프로필 이미지 업로드하기(수정 포함)")
    @ApiErrorCodeExample({
            MEMBER_NOT_FOUND,
            EMPTY_FILE_EXCEPTION,
            NO_FILE_EXTENSION,
            INVALID_FILE_EXTENSION,
            INVALID_FILE_URI,
            IO_EXCEPTION_ON_IMAGE_UPLOAD,
            IO_EXCEPTION_ON_IMAGE_DELETE
    })
    @PostMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> uploadMyProfileImg(@AuthenticatedUsername String username,
                                                  @RequestParam("file") MultipartFile file) {
        String profileImageURI = memberService.uploadMyProfileImage(username, file);
        return ApiResponse.onSuccess(profileImageURI);
    }


    @Operation(summary = "내 프로필 이미지 삭제하기",description = "내 프로필 이미지가 존재하는 경우 삭제합니다.")
    @ApiErrorCodeExample({
            MEMBER_NOT_FOUND,
            INVALID_FILE_URI,
            IO_EXCEPTION_ON_IMAGE_DELETE
    })
    @DeleteMapping("/profile-image")
    public ApiResponse<Boolean> deleteMyProfileImg(@AuthenticatedUsername String username){
        memberService.deleteMyProfileImage(username);
        return ApiResponse.onSuccess(true);
    }




    @Operation(summary = "서비스에서 탈퇴하기")
    @ApiErrorCodeExample({
            ErrorStatus.MEMBER_NOT_FOUND
    })
    @DeleteMapping
    public ApiResponse<Boolean> deleteMe(@AuthenticatedUsername String username) {
        memberService.deleteMeByUsername(username);
        return ApiResponse.onSuccess(true);
    }

    @Operation(summary = "내가 참여중인 워크스페이스 가져오기")
    @ApiErrorCodeExample({
            ErrorStatus.MEMBER_NOT_FOUND
    })
    @GetMapping("/workspaces")
    public ApiResponse<List<MyWorkspaceDto>> getMyWorkspaces(@AuthenticatedUsername String username) {
        return ApiResponse.onSuccess(memberService.getMyWorkspaces(username));
    }

    @Operation(summary = "워크스페이스에서 나가기",description = "해당 워크스페이스에서 나갑니다.")
    @ApiErrorCodeExample({
             MEMBER_NOT_FOUND,
            MEMBER_NOT_IN_WORKSPACE
    })
    @DeleteMapping("/workspaces/{workspaceId}")
    public ApiResponse<Boolean> leaveWorkspace(@AuthenticatedUsername String username,
                                                          @PathVariable Long workspaceId){
        workspaceService.leaveWorkspace(username, workspaceId);
        return ApiResponse.onSuccess(true);
    }



}
