package com.ktb.jpa_practice.dto;

import com.ktb.jpa_practice.entity.User;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserResponseDto {

    private Long userId;
    private String nickName;
    private String email;
    private String profileImageUrl;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public UserResponseDto(Long userId, String nickName, String email, String profileImageUrl, Timestamp createdAt, Timestamp updatedAt) {
        this.userId = userId;
        this.nickName = nickName;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getUserId(),
                user.getNickname(),
                user.getEmail(),
                user.getProfileImageUrl(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
