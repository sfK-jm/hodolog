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
import com.sfk.hodolog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    // 글이 너무 많은 경우 -> 비용이 너무 많이 든다.
    // 글이 -> 100,000,00 -> DB글 모두 조회하는 경우 -> DB가 뻗을 수 있음
    // DB ->  애플리케이션 서버로 전달하는 시간, 트래픽비용 등이 많이 발생할 수 있다.

    public List<PostResponse> getList(PostSearch postSearch) {
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id"));
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
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
