package com.ktb.ktb_community.service;

import com.ktb.ktb_community.common.Security.JwtProvider;
import com.ktb.ktb_community.common.Security.LoginRequestDto;
import com.ktb.ktb_community.common.Security.LoginResponseDto;
import com.ktb.ktb_community.common.Security.RefreshToken;
import com.ktb.ktb_community.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginResponseDto login(LoginRequestDto loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        String email = authentication.getName();

        String accessToken = jwtProvider.createAccessToken(email);
        String refreshToken = jwtProvider.createRefreshToken(email);

        refreshTokenRepository.save(new RefreshToken(email, refreshToken));

        return new LoginResponseDto(accessToken, refreshToken);
    }

    public void logout(String email) {

        RefreshToken refreshToken = refreshTokenRepository.findById(email)
                .orElse(null);

        if (refreshToken != null) {
            refreshTokenRepository.deleteById(email);
        }
    }

}
