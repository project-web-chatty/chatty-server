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


    @PostMapping("/signup")
    public ResponseEntity<MemberBriefDto> signup(@Valid @RequestBody final MemberJoinRequestDto memberJoinRequestDTO){

        MemberBriefDto me = memberService.signup(memberJoinRequestDTO);
        return ResponseEntity.ok().body(me);

    }

    @GetMapping("/{memberName}")
    public MemberBriefDto getMemberBriefProfile(@PathVariable String memberName){
        return memberService.getMemberProfileByMemberId(memberName);
    }





}
