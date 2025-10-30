package com.ktb.ktb_community.service;

import com.ktb.ktb_community.common.Security.LoginRequestDto;
import com.ktb.ktb_community.common.Security.LoginResponseDto;
import com.ktb.ktb_community.common.Security.SessionData;
import com.ktb.ktb_community.dto.UserResponseDto;
import com.ktb.ktb_community.entity.User;
import com.ktb.ktb_community.exception.NoPermissionException;
import com.ktb.ktb_community.exception.NotFoundException;
import com.ktb.ktb_community.repository.SessionIdRepository;
import com.ktb.ktb_community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionIdRepository sessionIdRepository;

    public LoginResponseDto login(LoginRequestDto loginRequest) {

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new NotFoundException("email", "not found"));

        if (user == null || !checkPassword(user, loginRequest.getPassword())) {

            throw new NoPermissionException("auth", "no_permission");
        }

        String sessionString = UUID.randomUUID().toString();

        SessionData sessionData = new SessionData(sessionString, user.getUserId());

        sessionIdRepository.save(sessionData);

        UserResponseDto userResponseDto =  UserResponseDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();

        return new LoginResponseDto(sessionString, userResponseDto);
    }

    public void logout(String sessionId) {

         sessionIdRepository.findById(sessionId).ifPresent(sessionIdRepository::delete);
    }

    private boolean checkPassword(User user, String rawPassword) {

        return passwordEncoder.matches(rawPassword, user.getPassword());
    }



}
