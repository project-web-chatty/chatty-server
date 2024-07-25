package com.messenger.chatty.controller;
import com.messenger.chatty.config.web.AuthenticatedUsername;
import com.messenger.chatty.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.dto.response.member.MemberBriefDto;
import com.messenger.chatty.presentation.ApiResponse;
import com.messenger.chatty.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "MEMBER API", description = "회원과 관련된 기본적인 API들입니다.")
@RequestMapping("/api/member")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;


    @Operation(summary = "회원가입하기(일반)")
    @PostMapping("/signup")
    public ApiResponse<MemberBriefDto> signup(@Valid @RequestBody MemberJoinRequestDto memberJoinRequestDTO){
        return ApiResponse.ok(memberService.signup(memberJoinRequestDTO));
    }

    @Operation(summary = "멤버(타인) 프로필 정보 가져오기")
    @GetMapping("/{memberId}")
    public ApiResponse<MemberBriefDto> getMemberBriefProfile(@PathVariable Long memberId){
        return ApiResponse.ok(memberService.getMemberProfileByMemberId(memberId));
    }

    @Operation(summary = "username(아이디) 중복 검사하기")
    @PostMapping("/check/username")
    public ApiResponse<Boolean> checkDuplicatedUsername(@AuthenticatedUsername String username){
        memberService.checkDuplicatedUsername(username);
        return ApiResponse.ok(true);
    }
}
