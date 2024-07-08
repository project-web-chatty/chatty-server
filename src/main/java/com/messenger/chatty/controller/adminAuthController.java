package com.messenger.chatty.controller;


import com.messenger.chatty.dto.request.MemberJoinRequestDTO;
import com.messenger.chatty.dto.response.MemberResponseDTO;
import com.messenger.chatty.dto.response.WorkspaceResponseDto;
import com.messenger.chatty.service.ChannelService;
import com.messenger.chatty.service.MemberService;
import com.messenger.chatty.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/admin") // for the admin of this web service
@RequiredArgsConstructor
@RestController
public class adminAuthController {
    private final MemberService memberService;
    private final WorkspaceService workspaceService;
    private final ChannelService channelService;


    @GetMapping("/members")
    public List<MemberResponseDTO> getAllMemberList(){

        return memberService.getAllMemberList();


    }


    @GetMapping("/workspaces")
    public List<WorkspaceResponseDto> getAllWorkspaceList(){

        return workspaceService.getAllWorkspaceList();

    }



}
