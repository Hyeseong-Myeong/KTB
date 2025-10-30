package com.ktb.ktb_community.common.Security;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public ResponseCookie createSessionIdCookie(String SessionId) {
        return ResponseCookie.from("sessionId", SessionId)
                .httpOnly(true)
                .secure(true)
                .path("/api")
                .maxAge(900)
                .sameSite("Lax")
                .build();
    }

    public ResponseCookie clearSessionIdCookie() {
        return ResponseCookie.from("sessionId", null)
                .maxAge(0)
                .path("/")
                .build();
    }
}
