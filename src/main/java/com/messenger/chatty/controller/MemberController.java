package com.messenger.chatty.controller;


import com.messenger.chatty.dto.request.MemberJoinRequestDTO;
import com.messenger.chatty.dto.response.MemberResponseDTO;
import com.messenger.chatty.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /*
    @GetMapping("/{memberId}")
    public List<MemberResponseDTO> getMemberList(@PathVariable Long memberId){
        // authentication logic should be included

        return ResponseEntity.ok().build();
    }*/






}
