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

    @Operation(summary = "해당 워크스페이스의 초대링크 생성하기",description = "워크 스페이스의 초대링크를 새로 갱신할때 사용합니다")
    @GetMapping("/{workspaceId}")
    public String getNewInvitationCode(Long workspaceId){

        return inviteService.getNewInvitationCode(workspaceId);

    }
    @Operation(summary = "해당 워크스페이스의 초대링크 가져오기")
    @PostMapping("/{workspaceId}")
    public String setInvitationCOde(Long workspaceId){

        return inviteService.getNewInvitationCode(workspaceId);

    }


    @Operation(summary = "워크 스페이스 초대 수락 요청",
            description = "초대링크를 브라우저창에 입력해서 웹서버 측에 페이지를 요청했을때, " +
                    "웹서버 측은 이 엔드포인트 단으로 곧바로 요청을 보낸다. 앞 필터에서 먼저 리프레시 토큰으로 로그인 여부를 확인하고 " +
                    "로그인 되어 있다면 이후 로직을 시행, 로그인 되어 있지 않으면 로그인페이지로 리다이렉팅 시킨다.")
    // 로그인 되었다고 가정
    @PostMapping("/{code}")
    public WorkspaceResponseDto acceptInvitationAndEnterToWorkspace(@PathVariable String code,@RequestParam String username){
        return inviteService.acceptInvitationAndEnterToWorkspace(username, code);
    }
}
