package com.ktb.ktb_community.common.Security;

import com.ktb.ktb_community.dto.UserResponseDto;
import lombok.Data;

@Data
public class LoginResponseDto {

    private String sessionId;

    private UserResponseDto userResponseDto;

    public LoginResponseDto(String sessionId, UserResponseDto userResponseDto) {
        this.sessionId = sessionId;
        this.userResponseDto = userResponseDto;
    }
}
