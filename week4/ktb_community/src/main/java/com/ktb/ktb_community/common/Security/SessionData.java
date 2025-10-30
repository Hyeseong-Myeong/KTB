package com.ktb.ktb_community.common.Security;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "SessionId", timeToLive = 900) //Redis 저장 될 때의 Prefix //TTL 15분
@NoArgsConstructor
public class SessionData {

    @Id
    private String sessionId;

    private String email;

    public SessionData(String sessionId, String email) {
        this.sessionId = sessionId;
        this.email = email;
    }
}
