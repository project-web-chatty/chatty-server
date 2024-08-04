package com.messenger.chatty.domain.member.controller;
import com.messenger.chatty.global.config.web.AuthenticatedUsername;
import com.messenger.chatty.domain.member.dto.request.MemberUpdateRequestDto;
import com.messenger.chatty.domain.member.dto.response.MyProfileDto;
import com.messenger.chatty.domain.workspace.dto.response.WorkspaceBriefDto;
import com.messenger.chatty.global.presentation.ApiResponse;
import com.messenger.chatty.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "MY DATA API", description = "유저 자신의 리소스 접근과 관련된 API입니다.")
@RequestMapping("/api/me")
@RequiredArgsConstructor
@RestController
public class MyDataController {
    private final MemberService memberService;

    @Operation(summary = "내 프로필 정보 가져오기")
    @GetMapping
    public ApiResponse<MyProfileDto> getMyProfile(@AuthenticatedUsername String username) {
        return ApiResponse.onSuccess(memberService.getMyProfileByUsername(username));
    }

    @Operation(summary = "내 프로필 정보 수정하기")
    @PutMapping
    public ApiResponse<Long> changeMyProfile(@AuthenticatedUsername String username,
                                                       @RequestBody @Valid MemberUpdateRequestDto updateRequestDto) {
        return ApiResponse.onSuccess(memberService.updateMyProfile(username, updateRequestDto));
    }


    @Operation(summary = "서비스에서 탈퇴하기")
    @DeleteMapping
    public ApiResponse<Boolean> deleteMe(@AuthenticatedUsername String username) {
        memberService.deleteMeByUsername(username);
        return ApiResponse.onSuccess(true);
    }

    @Operation(summary = "내가 참여중인 워크스페이스 가져오기")
    @GetMapping("/workspaces")
    public ApiResponse<List<WorkspaceBriefDto>> getMyWorkspaces(@AuthenticatedUsername String username) {
        return ApiResponse.onSuccess(memberService.getMyWorkspaces(username));
    }
}
