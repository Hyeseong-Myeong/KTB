package com.ktb.jpa_practice.controller;

import com.ktb.jpa_practice.dto.UserRequestDto;
import com.ktb.jpa_practice.dto.UserResponseDto;
import com.ktb.jpa_practice.entity.User;
import com.ktb.jpa_practice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto userRequestDto) {

        UserResponseDto userResponseDto =  userService.createUser(userRequestDto);

        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> findByUserId(@PathVariable Long userId) {

        UserResponseDto userResponseDto = userService.getUserById(userId);

        return ResponseEntity.ok(userResponseDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId, @RequestBody UserRequestDto userRequestDto) {

        UserResponseDto userResponseDto = userService.updateUser(userRequestDto);

        return ResponseEntity.ok(userResponseDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long userId) {

        userService.deleteUserById(userId);

        return ResponseEntity.ok(true);
    }

}
