package com.ktb.ktb_community.repository;


import com.ktb.ktb_community.common.Security.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByEmail(String email);
}