package com.ktb.jwt_prtc.filter;

import com.ktb.jwt_prtc.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private static final String[] EXCLUDED_PATHS = {
            "/login", "/refresh", "/error"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getRequestURI();
        return Arrays.stream(EXCLUDED_PATHS).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain chain
    )throws IOException, ServletException {

        boolean isIndex = isIndexRequest(request);
        Optional<String> token = extractToken(request);

        if(token.isEmpty()){
            if(isIndex){
                response.sendRedirect("/login");
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        if(!validateAndSetAttributes(token.get(), request)){
            if(isIndex){
                response.sendRedirect("/login");
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isIndexRequest(HttpServletRequest request){

        String uri = request.getRequestURI();
        return "/".equals(uri) || "/index".equals(uri);
    }

    private Optional<String> extractToken(HttpServletRequest request){
        return extractTokenFromHeader(request)
                .or(() -> extractTokenFromCookie(request));
    }

    private Optional<String> extractTokenFromHeader(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7));
    }

    private Optional<String> extractTokenFromCookie(HttpServletRequest request){
        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> "accessToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    private Boolean validateAndSetAttributes(String token, HttpServletRequest request){

        try{
            var jws = jwtProvider.parse(token);
            Claims body = jws.getBody();
            request.setAttribute("userId", Long.valueOf(body.getSubject()));
            request.setAttribute("role", body.get("role"));
            return true;
        } catch (Exception exception){
            return false;
        }
    }

}
