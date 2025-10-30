package com.ktb.ktb_community.common.Security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "Session", timeToLive = 900) //Redis 저장 될 때의 Prefix //TTL 15분
@NoArgsConstructor
@AllArgsConstructor
public class SessionData {

    @Id
    private String sessionId;

    private Long userId;


}
