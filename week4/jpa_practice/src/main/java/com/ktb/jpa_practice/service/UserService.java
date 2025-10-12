package com.ktb.jpa_practice.service;

import com.ktb.jpa_practice.dto.UserRequestDto;
import com.ktb.jpa_practice.dto.UserResponseDto;
import com.ktb.jpa_practice.entity.User;
import com.ktb.jpa_practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {

        //이메일 중복 검증
        if(userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new IllegalArgumentException();
        }

        //닉네임 중복 검증
        if(userRepository.existsByNickname(userRequestDto.getNickname())) {
            throw new IllegalArgumentException();
        }

        User user = User.builder()
                .email(userRequestDto.getEmail())
                .password(userRequestDto.getPassword())
                .profileImageUrl(userRequestDto.getProfileImageUrl())
                .nickname(userRequestDto.getNickname())
                .build();

        userRepository.save(user);

        return UserResponseDto.from(user);
    }

    @Transactional
    public UserResponseDto updateUser(UserRequestDto userRequestDto) {

        Optional<User> optionalUser = userRepository.findByEmail(userRequestDto.getEmail());

        if(optionalUser.isEmpty()) {
            throw new IllegalArgumentException();
        }

        User user = optionalUser.get();

        user.updateUser(userRequestDto.getProfileImageUrl());

        return UserResponseDto.from(user);
    }

    public UserResponseDto getUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        return UserResponseDto.from(user);
    }

    @Transactional
    public Boolean deleteUserById(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));

        userRepository.delete(user);
        return true;
    }

}
