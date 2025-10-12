package com.ktb.jpa_practice.repository;

import com.ktb.jpa_practice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
