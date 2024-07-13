package com.messenger.chatty.security;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


public class CookieGenerator {

    public static Cookie createCookie(String categoryKey, String value, Long minuteDuration) {
        Cookie cookie = new Cookie(categoryKey, value);
        cookie.setMaxAge(60*minuteDuration.intValue());
        // cookie.setSecure(true);
        // cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}
