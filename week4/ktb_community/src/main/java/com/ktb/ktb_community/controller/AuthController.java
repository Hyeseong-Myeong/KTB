package com.ktb.ktb_community.controller;

import com.ktb.ktb_community.common.Security.LoginResponseDto;
import com.ktb.ktb_community.common.advice.ApiResponse;
import com.ktb.ktb_community.common.Security.CookieUtil;
import com.ktb.ktb_community.common.Security.LoginRequestDto;
import com.ktb.ktb_community.dto.UserResponseDto;
import com.ktb.ktb_community.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService; // AuthService 주입
    private final CookieUtil cookieUtil;

    private static final int SESSION_EXPIRE_TIME = 15 * 60;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> login(@RequestBody LoginRequestDto loginRequest) {

        LoginResponseDto responseDto = authService.login(loginRequest);
        UserResponseDto dto = responseDto.getUserResponseDto();

        ResponseCookie sessionIdCookie = cookieUtil.createSessionIdCookie(responseDto.getSessionId());

        ApiResponse<UserResponseDto> successResponse = ApiResponse.success(
                "login_successful",
                new UserResponseDto(dto.getEmail(), dto.getNickname(), dto.getProfileImageUrl())
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, sessionIdCookie.toString())
                .body(successResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> logout(@CookieValue(value = "sessionId") String sessionId) {

        authService.logout(sessionId);
        ResponseCookie clearCookie = cookieUtil.clearSessionIdCookie();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                .build();
    }

    private void addSessionIdCookie(HttpServletResponse response, String email, String sessionId) {

        Cookie cookie = new Cookie(email, sessionId);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(SESSION_EXPIRE_TIME);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

}
