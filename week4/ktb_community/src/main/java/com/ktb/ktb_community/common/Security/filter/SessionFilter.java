package com.ktb.ktb_community.common.Security.filter;

import com.ktb.ktb_community.common.Security.SessionData;
import com.ktb.ktb_community.repository.SessionIdRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import software.amazon.awssdk.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 1) // 가장 먼저 실행되는 필터로 설정
public class SessionFilter extends OncePerRequestFilter {

    private final SessionIdRepository sessionIdRepository;

    private static final String[] EXCLUDED_PATHS = {
            "/api/users", //회원가입
            "/api/users/email", //중복체크
            "/api/users/nickname",
            "/error"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        //로그인 경로의 경우 필터 적용 하지 않음
        if(path.equals("/api/auth") && method.equals("POST")){
            return true;
        }

        return Arrays.stream(EXCLUDED_PATHS).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        Optional<String> optionalSessionId = extractSessionIdFromCookie(request);

        if(optionalSessionId.isEmpty()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //redis 조회하여 SessionId 대조
        String sessionId = optionalSessionId.get();
        Optional<SessionData> optionalSessionData = sessionIdRepository.findById(sessionId);

        if(optionalSessionData.isEmpty()) {
            // Redis에 세션이 없음 (만료 또는 무효)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 세션이 유효한 경우 TTL 갱신
        SessionData sessionData = optionalSessionData.get();
        sessionIdRepository.save(sessionData);

        //컨트롤러에 userId 전달
        request.setAttribute("userId", sessionData.getUserId());

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractSessionIdFromCookie(HttpServletRequest request){

        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> "sessionId".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
