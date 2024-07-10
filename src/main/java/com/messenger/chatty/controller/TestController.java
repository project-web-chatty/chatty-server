package com.messenger.chatty.controller;

import com.messenger.chatty.config.web.AuthenticatedUsername;
import com.messenger.chatty.dto.request.WorkspaceGenerateRequestDto;
import com.messenger.chatty.dto.response.WorkspaceResponseDto;
import com.messenger.chatty.service.MemberService;
import com.messenger.chatty.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/test")
@RequiredArgsConstructor
@RestController
public class TestController {
    private final MemberService memberService;
    private final WorkspaceService workspaceService;

    @DeleteMapping("/member/{memberId}")
    public ResponseEntity<Void> deleteMeInThisWebService(@PathVariable Long memberId) {
        System.out.println("memberId = " + memberId);
        memberService.deleteMeById(memberId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/workspace/{username}")
    public ResponseEntity<WorkspaceResponseDto> generateWorkspace(
            @Valid @RequestBody WorkspaceGenerateRequestDto workspaceGenerateRequestDto,
                                                  @PathVariable String username) {
        // 이것은 나중에 리팩터링 username은 토큰에 있음
        WorkspaceResponseDto workspaceResponseDto = workspaceService.generateWorkspace(workspaceGenerateRequestDto, username);
        return ResponseEntity.ok().body(workspaceResponseDto);
    }
}
