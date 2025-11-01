package com.ktb.ktb_community.common.Security;

import com.ktb.ktb_community.dto.UserResponseDto;
import lombok.Data;

@Data
public class LoginSuccessResponseDto {
    private String accessToken;

    private UserResponseDto userResponseDto;

    public LoginSuccessResponseDto(String accessToken, UserResponseDto userResponseDto) {
        this.accessToken = accessToken;
        this.userResponseDto = userResponseDto;
    }
}