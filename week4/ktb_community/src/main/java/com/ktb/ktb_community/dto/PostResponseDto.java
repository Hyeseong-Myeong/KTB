package com.ktb.ktb_community.dto;

import com.ktb.ktb_community.entity.Post;
import com.ktb.ktb_community.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
public class PostResponseDto {

    private Long postId;
    private String title;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer viewCount;
    private UserResponseDto user;

    private Integer likeCount;
    private Boolean liked;

    private List<ImageResponseDto> images;

    public PostResponseDto(Long postId, String title, String content, Timestamp createdAt, Timestamp updatedAt, Integer viewCount, UserResponseDto user) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
        this.user = user;
    }

    public PostResponseDto(Long postId, String title, String content, Timestamp createdAt, Timestamp updatedAt, Integer viewCount, UserResponseDto user, Integer likeCount, List<ImageResponseDto> images) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
        this.user = user;

        this.likeCount = likeCount;
        this.images = images;
    }

    public static PostResponseDto from(Post post) {
        return new PostResponseDto(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getViewCount(),
                UserResponseDto.from(post.getUser())
        );
    }

    public static PostResponseDto from(Post post, Integer likeCount, List<ImageResponseDto> images) {
        return new PostResponseDto(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getViewCount(),
                UserResponseDto.from(post.getUser()),
                likeCount,
                images
        );
    }
}
