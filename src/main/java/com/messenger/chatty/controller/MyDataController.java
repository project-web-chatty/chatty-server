package com.messenger.chatty.controller;

import com.messenger.chatty.dto.request.MemberUpdateRequestDto;
import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.member.MyProfileDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import com.messenger.chatty.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public MyProfileDto getMyProfile(@RequestParam String username) {
        return memberService.getMyProfileByUsername(username);
    }

    @Operation(summary = "내 프로필 정보 수정하기")
    @PutMapping
    public MemberBriefDto changeMyProfile(@RequestParam String username ,
                    @RequestBody @Valid MemberUpdateRequestDto updateRequestDto) {
        return memberService.updateMyProfile(username, updateRequestDto);
    }


    @Operation(summary = "서비스에서 탈퇴하기")
    @DeleteMapping
    public ResponseEntity<Void> deleteMe(@RequestParam String username) {
        memberService.deleteMeByUsername(username);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "내가 참여중인 워크스페이스 가져오기")
    @GetMapping("/workspaces")
    public List<WorkspaceBriefDto> getMyWorkspaces(@RequestParam String username ) {
        return memberService.getMyWorkspaces(username);
    }


    @Operation(summary = "특정 워크스페이스 내에 있는 채널 리스트 가져오기")
    @GetMapping("/channels")
    public List<ChannelBriefDto> getMyChannelsInWorkspace(@RequestParam String username
            , @RequestParam String workspaceName  ) {
        return memberService.getMyChannels(username,workspaceName);
    }

}
