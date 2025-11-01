package com.ktb.ktb_community.common.Security;

import com.ktb.ktb_community.dto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private UserResponseDto user;
}
