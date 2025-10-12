package com.ktb.jpa_practice.dto;

import com.ktb.jpa_practice.entity.Post;
import com.ktb.jpa_practice.entity.User;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class PostResponseDto {

    private Long postId;

    private String title;

    private String content;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Integer viewCount;


    private User user;

    public PostResponseDto(Long postId, String title, String content, Timestamp createdAt, Timestamp updatedAt, Integer viewCount, User user) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
        this.user = user;
    }

    public static PostResponseDto from(Post post) {
        return new PostResponseDto(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getViewCount(),
                post.getUser()
        );
    }
}
