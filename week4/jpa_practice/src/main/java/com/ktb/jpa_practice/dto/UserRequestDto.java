package com.ktb.jpa_practice.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserRequestDto {

    private String username;
    private String password;
    private String email;
    private String nickname;
    private String profileImageUrl;

}
