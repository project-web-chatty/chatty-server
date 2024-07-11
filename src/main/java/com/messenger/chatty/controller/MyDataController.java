package com.messenger.chatty.controller;

import com.messenger.chatty.dto.request.MemberUpdateRequestDto;
import com.messenger.chatty.dto.response.channel.ChannelBriefDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.member.MyProfileDto;
import com.messenger.chatty.dto.response.workspace.WorkspaceBriefDto;
import com.messenger.chatty.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/me")
@RequiredArgsConstructor
@RestController
public class MyDataController {
    private final MemberService memberService;

    @GetMapping
    public MyProfileDto getMyProfile(@RequestParam String username) {
        return memberService.getMyProfileByUsername(username);
    }
    @PutMapping
    public MemberBriefDto changeMyProfile(@RequestParam String username ,
                    @RequestBody @Valid MemberUpdateRequestDto updateRequestDto) {
        return memberService.updateMyProfile(username, updateRequestDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMe(@RequestParam String username) {
        memberService.deleteMeByUsername(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/workspaces")
    public List<WorkspaceBriefDto> getMyWorkspaces(@RequestParam String username ) {
        return memberService.getMyWorkspaces(username);
    }


    @GetMapping("/channels")
    public List<ChannelBriefDto> getMyChannelsInWorkspace(@RequestParam String username
            , @RequestParam String workspaceName  ) {
        return memberService.getMyChannels(username,workspaceName);
    }

}
