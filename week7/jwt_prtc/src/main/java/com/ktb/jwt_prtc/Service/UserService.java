package com.ktb.jwt_prtc.Service;

import com.ktb.jwt_prtc.entity.RefreshToken;
import com.ktb.jwt_prtc.entity.User;
import com.ktb.jwt_prtc.jwt.JwtProvider;
import com.ktb.jwt_prtc.repository.RefreshTokenRepository;
import com.ktb.jwt_prtc.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    private static final int ACCESS_TOKEN_EXPIRATION = 15 * 60;
    private static final int REFRESH_TOKEN_EXPIRATION = 14 * 24 * 3600;

    public User getUser(Long userId){
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional
    public String loginUser(String email, String password, HttpServletResponse response) {

        User user = userRepository.findByEmail(email).orElse(null);

        if(user == null || !checkPassword(user, password)){
            return null;
        }

        refreshTokenRepository.deleteByUserId(user.getId());

        var tokenResponse = generateAndSaveToken(user);

        addTokenCookies(response, tokenResponse);

        return tokenResponse.accessToken();
    }

    @Transactional
    public TokenResponse refreshToken(String refreshToken, HttpServletResponse response){

        var parsedRefreshToken = jwtProvider.parse(refreshToken);

        RefreshToken entity = refreshTokenRepository.findByTokenAndRevokedFalse(refreshToken).orElse(null);

        if(entity == null || entity.getExpiresAt().isBefore(Instant.now())){
            return null;
        }

        Long userId  = Long.valueOf(parsedRefreshToken.getBody().getSubject());
        User user = userRepository.findById(userId).orElse(null);

        if(user == null){
            return null;
        }

        String newAccessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail());

        addTokenCookie(response, "accessToken", newAccessToken, ACCESS_TOKEN_EXPIRATION);

        return new TokenResponse(newAccessToken, refreshToken);
    }

    public void logoutUser(HttpServletResponse response){

        addTokenCookie(response, "accessToken", null, 0);
        addTokenCookie(response, "refreshToken", null, 0);
    }

    private TokenResponse generateAndSaveToken(User user){

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        RefreshToken refreshEntity = new RefreshToken();
        refreshEntity.setUserId(user.getId());
        refreshEntity.setToken(refreshToken);
        refreshEntity.setExpiresAt(Instant.now().plusSeconds(ACCESS_TOKEN_EXPIRATION));
        refreshEntity.setRevoked(false);
        refreshTokenRepository.save(refreshEntity);

        return new TokenResponse(accessToken, refreshToken);
    }

    private void addTokenCookies(HttpServletResponse response, TokenResponse tokenResponse){

        addTokenCookie(response, "accessToken", tokenResponse.accessToken(), ACCESS_TOKEN_EXPIRATION);
        addTokenCookie(response, "refreshToken", tokenResponse.refreshToken(), REFRESH_TOKEN_EXPIRATION);
    }

    private void addTokenCookie(HttpServletResponse response, String name, String value, int maxAge){

        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    private boolean checkPassword(User user, String rawPassword){

        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public record TokenResponse(String accessToken, String refreshToken) {}

}
