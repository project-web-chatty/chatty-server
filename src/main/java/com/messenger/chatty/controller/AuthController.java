package com.messenger.chatty.controller;


import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.messenger.chatty.security.AuthService;
import com.messenger.chatty.security.CookieGenerator;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증,인가와 관련된 API")
@RequestMapping("/api/auth") // for the admin of this web service
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh_token")) {
                refreshToken = cookie.getValue();
            }
        }
        if (refreshToken == null) {
            //response status code
            return new ResponseEntity<>("refreshToken token null", HttpStatus.BAD_REQUEST);
        }
        DecodedJWT decodedJWT =null;
        //expired check
        try {
            decodedJWT = authService.verifyJWT(refreshToken);

        } catch (TokenExpiredException e) {
            //response status code
            return new ResponseEntity<>("refreshToken token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category =decodedJWT.getClaim("category").asString();

        if (!category.equals("refresh")) {
            System.out.println("cateogory:  "+category);
            //response status code
            return new ResponseEntity<>("invalid refreshToken token", HttpStatus.BAD_REQUEST);
        }

        // db에 저장되어 있는지 확인
        if (!authService.checkExistByToken(refreshToken)) {

            //response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }



        String username = decodedJWT.getSubject();
        String role = decodedJWT.getClaim("role").asString();

        //make new JWT
        String newAccessToken = authService.generateAccessToken(username,role);
        String newRefreshToken = authService.generateRefreshToken(username,role);

        // 리프레시 토큰 db에 갱신 하여 변경
        authService.deleteRefreshToken(refreshToken);
        authService.saveRefreshToken(newRefreshToken,username);



        //response
        response.addHeader("Authorization", "Bearer " + newAccessToken);
        response.addCookie(CookieGenerator.generateCookie("refresh_token", newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
