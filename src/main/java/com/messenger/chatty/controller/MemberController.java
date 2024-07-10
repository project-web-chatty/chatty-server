package com.messenger.chatty.controller;


import com.messenger.chatty.config.web.AuthenticatedUsername;
import com.messenger.chatty.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.dto.request.MemberProfileUpdateRequestDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.member.MyProfileDto;
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
       // validateJoinRequest(memberJoinReqDTO);
        MemberBriefDto me = memberService.signup(memberJoinRequestDTO);
        return ResponseEntity.ok().body(me);

    }

    @GetMapping("/{memberId}")
    public MemberBriefDto getMemberProfile(@PathVariable Long memberId){
        return memberService.findMemberProfileByMemberId(memberId);
    }


    @GetMapping("/me")
    public MyProfileDto getMyProfile(@AuthenticatedUsername String username) {
        return memberService.findMyProfileByUsername(username);
    }
    @PutMapping("/me")
    public ResponseEntity<MemberBriefDto> changeMyProfile(@AuthenticatedUsername String username ,
                                                  @RequestBody MemberProfileUpdateRequestDto updateRequestDTO) {
        MyProfileDto myProfileDto = memberService.updateMyProfile(username, updateRequestDTO);
        return ResponseEntity.ok().body(myProfileDto);
    }


    // 수정 필요
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMeInThisWebService(@AuthenticatedUsername String username) {
        memberService.deleteMeByUsername(username);
        return ResponseEntity.ok().build();
    }


}
