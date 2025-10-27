package com.ktb.jwt_prtc.config;

import com.ktb.jwt_prtc.entity.User;
import com.ktb.jwt_prtc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class SeedConfig {

    private final UserRepository userRepository;

    @Bean
    ApplicationRunner seedRunner() {
        return arguments -> seed();
    }

    @Transactional
    void seed() {

        if (userRepository.count() >= 10) return;

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            String rawPassword = "123aS!" + i;
            String encodedPassword = passwordEncoder.encode(rawPassword);
            User user = new User("tester" + i + "@test.com", encodedPassword, "tester" + i);
            userRepository.save(user);
        });
    }
}
