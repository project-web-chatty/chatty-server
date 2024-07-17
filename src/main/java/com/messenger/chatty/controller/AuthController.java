package com.messenger.chatty.controller;


import com.messenger.chatty.dto.request.LoginRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 및 토큰 관련 API")
@RequestMapping("/api/auth") // for the admin of this web service
@RequiredArgsConstructor
@RestController
public class AuthController {
    @Operation(summary = "로그인",description = "리프레시 토큰은 쿠키에 발급되며 엑세스토큰은 Authorization 헤더에 발급됩니다.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto)
    { // 스웨거 문서화를 위한 형식상 메서드
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Operation(summary = "로그아웃",description = "리프레시토큰이 disable 됩니다.")
    @PostMapping("/logout")
    public ResponseEntity<?> logout()
    { // 스웨거 문서화를 위한 형식상 메서드
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "엑세스토큰 및 리프레시 토큰 재발급",description = "기한이 짧은 엑세스 토큰이 만료 시 이곳으로 요청을 보내세요.")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue() {
        // 스웨거 문서화를 위한 형식상 메서드
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
