package com.messenger.chatty.domain.member.controller;
import com.messenger.chatty.domain.member.dto.request.MemberJoinRequestDto;
import com.messenger.chatty.domain.member.dto.response.MemberBriefDto;
import com.messenger.chatty.global.presentation.ApiResponse;
import com.messenger.chatty.domain.member.service.MemberService;
import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.annotation.api.ApiErrorCodeExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "MEMBER API", description = "회원과 관련된 기본적인 API들입니다.")
@RequestMapping("/api/member")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;


    @Operation(summary = "회원가입하기(일반)")
    @ApiErrorCodeExample({
            ErrorStatus.MEMBER_USERNAME_ALREADY_EXISTS
    })
    @PostMapping("/signup")
    public ApiResponse<Long> signup(@Valid @RequestBody MemberJoinRequestDto memberJoinRequestDTO){
        return ApiResponse.onSuccess(memberService.signup(memberJoinRequestDTO));
    }

    @Operation(summary = "멤버(타인) 프로필 정보 가져오기",description = "이 응답의 role field 는 ROLE_USER 이거나 ROLE_ADMIN입니다. 이 역할은 웹서비스에서의 일반 유저/어드민 여부를 알려주는 것이며, 워크스페이스 내에서의 역할이 아닙니다.")
    @ApiErrorCodeExample({
            ErrorStatus.MEMBER_NOT_FOUND
    })
    @GetMapping("/{memberId}")
    public ApiResponse<MemberBriefDto> getMemberBriefProfile(@PathVariable Long memberId){
        return ApiResponse.onSuccess(memberService.getMemberProfileByMemberId(memberId));
    }

    @Operation(summary = "username(아이디) 중복 검사하기")
    @ApiErrorCodeExample({
            ErrorStatus.MEMBER_USERNAME_ALREADY_EXISTS
    })
    @PostMapping("/check")
    public ApiResponse<Boolean> checkDuplicatedUsername(@RequestParam String username){
        memberService.checkDuplicatedUsername(username);
        return ApiResponse.onSuccess(true);
    }
}
