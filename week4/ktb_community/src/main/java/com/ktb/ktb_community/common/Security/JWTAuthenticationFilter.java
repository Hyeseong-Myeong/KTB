package com.ktb.ktb_community.common.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import software.amazon.awssdk.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private static final String[] EXCLUDED_PATHS = {
            "/api/users", //회원가입
            "/api/users/email", //중복체크
            "/api/users/nickname",
            "/error"
    };


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

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

        String token = extractTokenFromHeader(request);

        if(token == null || !jwtProvider.validateToken(token)) {
            sendErrorResponse(response, HttpStatus.SC_UNAUTHORIZED, "Invalid or Expired Access Token");
            return;
        }

        try {
            Long userId = Long.valueOf(jwtProvider.getUserIdFromToken(token));

            request.setAttribute("userId", userId);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            sendErrorResponse(response, HttpStatus.SC_UNAUTHORIZED, "Error processing token");
        }
    }


    private String extractTokenFromHeader(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7).trim();
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"message\": \"%s\"}", message));
    }

}
