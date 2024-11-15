package com.sfk.hodolog.service;

import com.sfk.hodolog.domain.Post;
import com.sfk.hodolog.repository.PostRepository;
import com.sfk.hodolog.request.PostCreate;
import com.sfk.hodolog.request.PostSearch;
import com.sfk.hodolog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        //postCreate -> entity
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다."));

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
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
}
