package com.messenger.chatty.controller;


import com.messenger.chatty.dto.request.MemberJoinRequestDTO;
import com.messenger.chatty.dto.response.MemberProfileResponseDto;
import com.messenger.chatty.dto.response.MyProfileResponseDto;
import com.messenger.chatty.exception.custom.UnexpectedNotAuthenticatedException;
import com.messenger.chatty.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public MyProfileResponseDto findMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // security should be constructed at the front filters;
        // basically, authorization will be carried out in front of this controller
        // but for the worse possible situation, verify session
        if(authentication.getPrincipal().toString().equals("anonymousUser"))
            throw new UnexpectedNotAuthenticatedException("your authentication is invalid");

        String username= ((UserDetails)authentication.getPrincipal()).getUsername();
        return memberService.findMyProfileByUsername(username);
    }

    @GetMapping("/{memberId}")
    public MemberProfileResponseDto getMemberList(@PathVariable Long memberId){
        // authentication logic should be included

        return memberService.findMemberProfileByMemberId(memberId);
    }






}
