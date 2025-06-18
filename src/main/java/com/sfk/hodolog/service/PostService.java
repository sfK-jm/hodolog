package com.sfk.hodolog.service;

import com.sfk.hodolog.domain.Post;
import com.sfk.hodolog.domain.PostEditor;
import com.sfk.hodolog.exception.PostNotFound;
import com.sfk.hodolog.exception.UserNotFound;
import com.sfk.hodolog.repository.post.PostRepository;
import com.sfk.hodolog.repository.UserRepository;
import com.sfk.hodolog.request.post.PostCreate;
import com.sfk.hodolog.request.post.PostEdit;
import com.sfk.hodolog.request.post.PostSearch;
import com.sfk.hodolog.response.PagingResponse;
import com.sfk.hodolog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public void write(Long userId, PostCreate postCreate) {
        var user = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        //postCreate -> entity
        Post post = Post.builder()
                .user(user)
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        log.info("write: {}", postCreate);
        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return new PostResponse(post);
    }

    public PagingResponse<PostResponse> getList(PostSearch postSearch) {
        Page<Post> postPage = postRepository.getList(postSearch);
        PagingResponse<PostResponse> postList = new PagingResponse<>(postPage, PostResponse.class);
        return postList;
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        PostEditor postEditor = editorBuilder
                .title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        postRepository.delete(post);
    }
}
