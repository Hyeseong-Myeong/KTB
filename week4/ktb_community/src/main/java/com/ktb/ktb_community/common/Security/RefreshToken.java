package com.ktb.ktb_community.common.Security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 1209600) //14일
@NoArgsConstructor
public class RefreshToken {

    @Id
    private String email;

    private String token;

    public RefreshToken(String email, String token) {
        this.email = email;
        this.token = token;
    }
}