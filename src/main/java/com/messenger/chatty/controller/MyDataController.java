package com.messenger.chatty.controller;

import com.messenger.chatty.config.web.AuthenticatedUsername;
import com.messenger.chatty.dto.request.MemberProfileUpdateRequestDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.dto.response.member.MyProfileDto;
import com.messenger.chatty.service.MemberService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/api/me")
@RequiredArgsConstructor
@RestController
public class MyDataController {
    private final MemberService memberService;

    @GetMapping
    public MyProfileDto getMyProfile(@AuthenticatedUsername String username) {
        return memberService.findMyProfileByUsername(username);
    }
    @PutMapping
    public ResponseEntity<MemberBriefDto>
    changeMyProfile(@AuthenticatedUsername String username ,
                    @RequestParam(name = "name", required = false)  @Size(max = 10)  String name,
                    @RequestParam(name = "nickname", required = false) @Size(max = 10)   String  nickname,
                    @RequestParam(name = "introduction", required = false) @Size(max = 500)  String  introduction) {

        MyProfileDto myProfileDto = memberService.updateMyProfile(username, name,nickname,introduction);
        return ResponseEntity.ok().body(myProfileDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMeInThisWebService(@AuthenticatedUsername String username) {
        memberService.deleteMeByUsername(username);
        return ResponseEntity.ok().build();
    }

}
