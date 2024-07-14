package com.messenger.chatty.controller;


import com.messenger.chatty.dto.response.workspace.WorkspaceResponseDto;
import com.messenger.chatty.service.InviteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "워크스페이스 초대코드 API", description = "초대코드 생성, 리프레싱 관련 API입니다.")
@RequestMapping("/api/invite") // for the admin of this web service
@RequiredArgsConstructor
@RestController
public class InviteController {
    private final InviteService inviteService;

    @Operation(summary = "해당 워크스페이스의 초대링크 가져오기")
    @GetMapping("/{workspaceId}")
    public String getNewInvitationCode(@PathVariable  Long workspaceId){

        return inviteService.getNewInvitationCode(workspaceId);

    }

    @Operation(summary = "해당 워크스페이스의 초대링크 생성하기",description = "워크 스페이스의 초대링크를 새로 갱신할때 사용합니다")
    @PostMapping("/{workspaceId}")
    public String generateInvitationCode(@PathVariable Long workspaceId){

        return inviteService.setInvitationCode(workspaceId);

    }

}
