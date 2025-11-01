package com.ktb.ktb_community.service;

import com.ktb.ktb_community.common.Security.*;
import com.ktb.ktb_community.dto.UserResponseDto;
import com.ktb.ktb_community.entity.User;
import com.ktb.ktb_community.exception.NoPermissionException;
import com.ktb.ktb_community.exception.NotFoundException;
import com.ktb.ktb_community.repository.RefreshTokenRepository;
import com.ktb.ktb_community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new NotFoundException("email", "email_not_found"));

        if(!checkPassword(user, loginRequest.getPassword())) {
            throw new NoPermissionException("auth", "no_permission");
        }

        refreshTokenRepository.deleteById(user.getUserId());

        TokenResponse tokenResponse = generateAndSaveToken(user);

        return new LoginResponseDto(tokenResponse.accessToken, tokenResponse.refreshToken, UserResponseDto.from(user));
    }

    public LoginResponseDto refreshToken(String refreshToken) {

        var parsedToken = jwtProvider.parseToken(refreshToken);

        RefreshToken entity = refreshTokenRepository.findByToken(refreshToken).orElseThrow(() -> new NoPermissionException("token", "token_not_found"));

        Long userId = entity.getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user", "user_not_found"));

        String newAccessToken = jwtProvider.createAccessToken(user.getUserId());
        return new LoginResponseDto(newAccessToken, refreshToken, UserResponseDto.from(user));
    }

    public void logout(Long userId) {

        RefreshToken refreshToken = refreshTokenRepository.findById(userId)
                .orElse(null);

        if (refreshToken != null) {
            refreshTokenRepository.deleteById(userId);
        }
    }


    private TokenResponse generateAndSaveToken(User user) {

        String accessTokenString = jwtProvider.createAccessToken(user.getUserId());
        String refreshTokenString = jwtProvider.createRefreshToken(user.getUserId());

        RefreshToken refreshToken = new RefreshToken(user.getUserId(), refreshTokenString);
        refreshTokenRepository.save(refreshToken);

        return new TokenResponse(accessTokenString, refreshTokenString);
    }

    private boolean checkPassword(User user, String rawPassword){

        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public record TokenResponse(String accessToken, String refreshToken) {}

}
