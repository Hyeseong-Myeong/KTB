package com.ktb.ktb_community.repository;


import com.ktb.ktb_community.common.Security.SessionId;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionIdRepository extends CrudRepository<SessionId, String> {

    Optional<SessionId> findByEmail(String email);
}