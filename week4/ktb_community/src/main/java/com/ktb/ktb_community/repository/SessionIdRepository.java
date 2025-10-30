package com.ktb.ktb_community.repository;


import com.ktb.ktb_community.common.Security.SessionData;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionIdRepository extends CrudRepository<SessionData, String> {

    Optional<SessionData> findByEmail(String email);
}