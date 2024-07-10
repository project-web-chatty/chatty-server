package com.messenger.chatty.controller;


import com.messenger.chatty.config.web.AuthenticatedUsername;
import com.messenger.chatty.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceResponseDto;
import com.messenger.chatty.service.MemberService;
import com.messenger.chatty.service.WorkspaceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/workspace")
@RequiredArgsConstructor
@RestController
public class WorkspaceController {
    private final MemberService memberService;
    private final WorkspaceService workspaceService;


    // 워크 스페이스 생성
    @PostMapping
    public ResponseEntity<WorkspaceBriefDto> createWorkspace(@AuthenticatedUsername String username,
                                                             @Valid @RequestBody WorkspaceGenerateRequestDto workspaceGenerateRequestDto
            ) {
        WorkspaceBriefDto workspaceBriefDto = workspaceService.generateWorkspace(workspaceGenerateRequestDto, username);
        return ResponseEntity.ok().body(workspaceBriefDto);
    }


    // 워크 스페이스 정보 가져오기
    // 채널 목록, 멤버 목록을 포함한 워크스페이스 정보를 가져온다
    // 워크스페이스 내 멤버만 인가되는 요청들에 대해서는 앞의 필터에서 인가를 구현한다.
    @GetMapping("/{workspaceName}")
    public ResponseEntity<WorkspaceResponseDto> getWorkspaceProfile
            (@PathVariable String workspaceName) {
        WorkspaceResponseDto workspaceResponseDto = workspaceService.getWorkspaceProfile(workspaceName);
        return ResponseEntity.ok().body(workspaceResponseDto);
    }

    // 워크 스페이스 정보 수정하기
    @PutMapping("/{workspaceName}")
    public ResponseEntity<WorkspaceBriefDto> updateWorkspaceProfile
            (@PathVariable String workspaceName ,
             @RequestParam(name = "profile_img", required = false)  String profile_img,
             @RequestParam(name = "description", required = false) @Size(max = 500)  String  description
             ) {
        WorkspaceBriefDto workspaceBriefDto = workspaceService.updateWorkspaceProfile(workspaceName, profile_img, description);
        return ResponseEntity.ok().body(workspaceBriefDto);
    }
}
