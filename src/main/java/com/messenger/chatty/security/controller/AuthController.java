package com.messenger.chatty.security.controller;


import com.messenger.chatty.domain.member.dto.request.LoginRequestDto;
import com.messenger.chatty.domain.member.dto.request.PasswordUpdateRequestDto;
import com.messenger.chatty.domain.refresh.dto.response.TokenResponseDto;
import com.messenger.chatty.global.config.web.AuthenticatedUsername;
import com.messenger.chatty.global.presentation.ApiResponse;
import com.messenger.chatty.global.presentation.ErrorStatus;
import com.messenger.chatty.global.presentation.annotation.api.ApiErrorCodeExample;
import com.messenger.chatty.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 및 토큰 관련 API")
@RequestMapping("/api/auth") // for the admin of this web service
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "로그인",description = "리프레시 토큰과 엑세스토큰이 body에 담겨 응답됩니다. 리프레시 토큰의 주기는 7일, 엑세스 토큰의 주기는 10분입니다.")
    @ApiErrorCodeExample({
            ErrorStatus.AUTH_FAIL_LOGIN
    })
    @PostMapping("/login")
    public ApiResponse<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto)
    {
        return ApiResponse.onSuccess(authService.login(loginRequestDto));
    }

    @Operation(summary = "로그아웃", description = "헤더에 엑세스토큰을 담아 요청을 보내면 리프레시토큰이 disable 되며 정상 로그아웃됩니다.")
    @ApiErrorCodeExample
    @PostMapping("/logout")
    public ApiResponse<Boolean> logout(HttpServletRequest request)
    {
        authService.logout(request);
        return ApiResponse.onSuccess(true);
    }

    @Operation(summary = "엑세스토큰 및 리프레시 토큰 재발급",description = "기한이 짧은 엑세스 토큰이 만료 시 헤더에 리프레시토큰을 담아 요청을 보내세요. body에 리프레시&엑세스 토큰이 담겨 응답됩니다.")
    @ApiErrorCodeExample({
            ErrorStatus.AUTH_INVALID_TOKEN,
            ErrorStatus.AUTH_OUTDATED_REFRESH_TOKEN
    })
    @PostMapping("/reissue")
    public ApiResponse<TokenResponseDto> reissue(HttpServletRequest request) {
            return  ApiResponse.onSuccess(authService.reissueToken(request));
    }

    @Operation(summary = "비밀번호 변경",description = "oldPassword는 기존의 것, newPassword는 새 비밀번호입니다. 기존 비밀번호와 새 비밀번호의 불일치 여부 등의 검증은 프론트 단에서 진행 해주세요")
    @ApiErrorCodeExample({
            ErrorStatus.AUTH_FAIL_LOGIN,
            ErrorStatus.AUTH_FAIL_PASSWORD_MATCHING
    })
    @PostMapping("/password")
    public ApiResponse<Boolean> changePassword(@AuthenticatedUsername String username,
                                               @RequestBody PasswordUpdateRequestDto passwordUpdateRequestDto) {
        authService.changePassword(username,passwordUpdateRequestDto);
        return  ApiResponse.onSuccess(true);
    }

}
