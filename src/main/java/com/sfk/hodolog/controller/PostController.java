package com.sfk.hodolog.controller;

import com.sfk.hodolog.config.UserPrincipal;
import com.sfk.hodolog.request.post.PostCreate;
import com.sfk.hodolog.request.post.PostEdit;
import com.sfk.hodolog.request.post.PostSearch;
import com.sfk.hodolog.response.PagingResponse;
import com.sfk.hodolog.response.PostResponse;
import com.sfk.hodolog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/posts")
    public void post(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid PostCreate request) {
        postService.write(userPrincipal.getUserId(), request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }


    /**
     * /post -> 글 전체 조회(검색 + 페이징)
     * /posts/{postId} -> 글 한개만 조회
     */

    //조회 API
    @GetMapping("/posts")
    public PagingResponse<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        postService.edit(postId, request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') && hasPermission(#postId, 'POST', 'DELETE')")
    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
