package com.ktb.ktb_community.service;

import com.ktb.ktb_community.dto.*;
import com.ktb.ktb_community.entity.Image;
import com.ktb.ktb_community.entity.Post;
import com.ktb.ktb_community.entity.User;
import com.ktb.ktb_community.exception.NoPermissionException;
import com.ktb.ktb_community.repository.ImageRepository;
import com.ktb.ktb_community.repository.PostLikeRepository;
import com.ktb.ktb_community.repository.PostRepository;
import com.ktb.ktb_community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public PostResponseDto create(PostRequestDto requestDto, String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("user not found"));
        List<ImageResponseDto> images = new ArrayList<>();

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .user(user)
                .build();

        Long postId = postRepository.save(post).getPostId();


        return PostResponseDto.from(post,0, images);
    }

    public PostResponseDto getPostById(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("post not found"));
        Integer likeCount = postLikeRepository.countByPostId(postId);
        List<Image> images = imageRepository.findByPostId(postId);

        List<ImageResponseDto> dtos =  images.stream()
                                             .map(ImageResponseDto::from)
                                            .toList();

        return PostResponseDto.from(post,likeCount, dtos);
    }

    public PostPageResponseDto getPosts(Integer cursor, int size){

        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "postId"));

        Slice<Post> postSlice = (cursor == null)
                ? postRepository.findAllByOrderByPostIdDesc(pageable)
                : postRepository.findByPostIdLessThanOrderByPostIdDesc(Long.valueOf(cursor), pageable);

        List<PostResponseDto> postresponseDtoList = postSlice.getContent().stream()
                .map(PostResponseDto::from)
                .toList();

        Long nextCursor = null;
        if (!postresponseDtoList.isEmpty()) {
            nextCursor = postresponseDtoList.get(postresponseDtoList.size() - 1).getPostId();
        }

        CursorDto cursorDto = new CursorDto(nextCursor, postSlice.hasNext());
        return new PostPageResponseDto(postresponseDtoList, cursorDto);
    }

    @Transactional
    public PostResponseDto updatePost(PostRequestDto postRequestDto, Long postId, String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("user not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("post not found"));

        if(!post.getUser().getEmail().equals(user.getEmail())) {
            throw new NoPermissionException("user", "NoPermission");
        }

        post.update(postRequestDto.getTitle(), postRequestDto.getContent());

        return PostResponseDto.from(postRepository.save(post));
    }

    @Transactional
    public void deletePostById(Long postId, String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("user not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("post not found"));

        if(!post.getUser().getEmail().equals(user.getEmail())) {
            throw new NoPermissionException("user", "NoPermission");
        }

        postRepository.delete(post);
    }


}
