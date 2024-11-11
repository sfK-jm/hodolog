package com.sfk.hodolog.service;

import com.sfk.hodolog.domain.Post;
import com.sfk.hodolog.repository.PostRepository;
import com.sfk.hodolog.request.PostCreate;
import com.sfk.hodolog.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        //given

        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        //when
        postService.write(postCreate);

        //then
        Assertions.assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        // given

        Post requestPost = Post.builder()
                .title("제목")
                .content("컨텐츠")
                .build();
        postRepository.save(requestPost);

        // when
        PostResponse response = postService.get(requestPost.getId());

        // then
        assertNotNull(response);

        assertEquals(1L, postRepository.count());
        assertEquals("제목", response.getTitle());
        assertEquals("컨텐츠", response.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test3() {
        // given

        postRepository.saveAll(List.of(
                Post.builder()
                        .title("제목1")
                        .content("컨텐츠1")
                        .build(),
                Post.builder()
                        .title("제목2")
                        .content("컨텐츠2")
                        .build()

        ));

        // when
        List<PostResponse> posts = postService.getList();

        // then
        assertEquals(2L, posts.size());

    }


}