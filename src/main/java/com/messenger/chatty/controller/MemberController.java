package com.messenger.chatty.controller;


import com.messenger.chatty.config.web.AuthenticatedUsername;
import com.messenger.chatty.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.dto.request.MemberProfileUpdateRequestDto;
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
    public ResponseEntity<Void> signup(@Valid @RequestBody final MemberJoinRequestDto memberJoinRequestDTO){
       // validateJoinRequest(memberJoinReqDTO);
        memberService.signup(memberJoinRequestDTO);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/{memberId}")
    public MemberProfileResponseDto getMemberProfile(@PathVariable Long memberId){
        // authentication logic should be included

        return memberService.findMemberProfileByMemberId(memberId);
    }


    @GetMapping("/me")
    public MyProfileResponseDto getMyProfile(@AuthenticatedUsername String username) {
        return memberService.findMyProfileByUsername(username);
    }
    @PutMapping("/me")
    public ResponseEntity<Void> changeMyProfile(@AuthenticatedUsername String username ,
                                                  @RequestBody MemberProfileUpdateRequestDto updateRequestDTO) {
        memberService.updateMyProfile(username,updateRequestDTO);
        return ResponseEntity.ok().build();
    }


    // 수정 필요
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMeInThisWebService(@AuthenticatedUsername String username) {
        memberService.deleteMeByUsername(username);
        return ResponseEntity.ok().build();
    }


}
