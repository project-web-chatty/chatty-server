package com.messenger.chatty.security;

import jakarta.servlet.http.Cookie;


public class CookieGenerator {

    public static Cookie generateCookie(String categoryKey, String value) {
        Cookie cookie = new Cookie(categoryKey, value);
        cookie.setMaxAge(7 * 24 * 60 * 60); // 1주일 동안 유효
        // cookie.setSecure(true);
         cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}
