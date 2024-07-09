package com.messenger.chatty.controller;


import com.messenger.chatty.config.web.AuthenticatedUsername;
import com.messenger.chatty.dto.request.MemberJoinRequestDTO;
import com.messenger.chatty.dto.response.MemberProfileResponseDto;
import com.messenger.chatty.dto.response.MyProfileResponseDto;
import com.messenger.chatty.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/member")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;



    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody final MemberJoinRequestDTO memberJoinRequestDTO){
       // validateJoinRequest(memberJoinReqDTO);
        memberService.signup(memberJoinRequestDTO);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/me")
    public MyProfileResponseDto getMyProfile(@AuthenticatedUsername String username) {
        return memberService.findMyProfileByUsername(username);
    }

    @GetMapping("/{memberId}")
    public MemberProfileResponseDto getMemberProfile(@PathVariable Long memberId){
        // authentication logic should be included

        return memberService.findMemberProfileByMemberId(memberId);
    }






}
