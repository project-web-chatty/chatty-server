package com.messenger.chatty.controller;


import com.messenger.chatty.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
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


    @PostMapping
    public MemberBriefDto signup(@Valid @RequestBody final MemberJoinRequestDto memberJoinRequestDTO){
        return memberService.signup(memberJoinRequestDTO);
    }

    @GetMapping("/{memberId}")
    public MemberBriefDto getMemberBriefProfile(@PathVariable Long memberId){
        return memberService.getMemberProfileByMemberId(memberId);
    }





}
