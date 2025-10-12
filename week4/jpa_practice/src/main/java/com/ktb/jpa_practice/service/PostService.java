package com.ktb.jpa_practice.service;

import com.ktb.jpa_practice.dto.PostRequestDto;
import com.ktb.jpa_practice.dto.PostResponseDto;
import com.ktb.jpa_practice.entity.Post;
import com.ktb.jpa_practice.entity.User;
import com.ktb.jpa_practice.repository.PostRepository;
import com.ktb.jpa_practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponseDto savePost(PostRequestDto postRequestDto) {

        Optional<User> optionalUser =  userRepository.findByEmail(postRequestDto.getEmail());

        if(optionalUser.isEmpty()) {
            throw new IllegalArgumentException();
        }

        User user = optionalUser.get();

        Post post = Post.builder()
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .user(user)
                .build();

        user.addPost(post);
        postRepository.save(post);

        return PostResponseDto.from(post);
    }

    public PostResponseDto getPostById(Long id) {

        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물"));

        return PostResponseDto.from(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto) {

        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물"));

        post.updatePost(postRequestDto.getTitle(), postRequestDto.getContent());

        return PostResponseDto.from(post);
    }

    @Transactional
    public Boolean deletePostById(Long id) {

        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물"));

        postRepository.delete(post);

        return true;
    }
}
