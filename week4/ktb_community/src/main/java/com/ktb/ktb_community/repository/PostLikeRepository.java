package com.ktb.ktb_community.repository;

import com.ktb.ktb_community.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Integer countByPost_PostId(Long postId);
}
