package com.ktb.ktb_community.dto;

import com.ktb.ktb_community.entity.PostLike;
import lombok.Data;

@Data
public class PostLikeResponseDto {

    Integer PostLikeCount;
    Boolean isLiked;

    public PostLikeResponseDto(Integer PostLikeCount, Boolean isLiked) {
        this.PostLikeCount = PostLikeCount;
        this.isLiked = isLiked;
    }
}
