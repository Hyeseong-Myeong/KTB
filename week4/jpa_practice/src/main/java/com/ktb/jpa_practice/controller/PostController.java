package com.ktb.jpa_practice.controller;

import com.ktb.jpa_practice.dto.PostRequestDto;
import com.ktb.jpa_practice.dto.PostResponseDto;
import com.ktb.jpa_practice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long postId) {

        PostResponseDto postResponseDto = postService.getPostById(postId);
        return ResponseEntity.ok(postResponseDto);
    }

    @PostMapping()
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto) {

        PostResponseDto postResponseDto = postService.savePost(postRequestDto);

        return ResponseEntity.ok(postResponseDto);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto) {

        PostResponseDto postResponseDto = postService.updatePost(postId, postRequestDto);

        return ResponseEntity.ok(postResponseDto);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deletePost(@PathVariable Long postId) {

        postService.deletePostById(postId);

        return ResponseEntity.ok(true);
    }
}
