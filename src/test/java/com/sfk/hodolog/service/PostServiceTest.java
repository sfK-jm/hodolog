package com.sfk.hodolog.service;

import com.sfk.hodolog.domain.Post;
import com.sfk.hodolog.domain.Users;
import com.sfk.hodolog.exception.PostNotFound;
import com.sfk.hodolog.repository.post.PostRepository;
import com.sfk.hodolog.repository.UserRepository;
import com.sfk.hodolog.request.post.PostCreate;
import com.sfk.hodolog.request.post.PostEdit;
import com.sfk.hodolog.request.post.PostSearch;
import com.sfk.hodolog.response.PagingResponse;
import com.sfk.hodolog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        //given

        var user = Users.builder()
                .name("test")
                .email("a@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);

        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        //when
        postService.write(user.getId(), postCreate);

        //then
        assertEquals(1L, postRepository.count());
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
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("타이틀 - " + i)
                        .content("컨텐츠 - " + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        //sql -> select limit offset

        // when

        //페이징은 0부터 시작
        PagingResponse<PostResponse> posts = postService.getList(postSearch);

        // then
        assertEquals(10L, posts.getSize());
        assertEquals("타이틀 - 19", posts.getItems().get(0).getTitle());
        assertEquals("타이틀 - 10", posts.getItems().get(9).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test4() {
        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("타이틀")
                .content("내용")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다. id =" +  post.getId()));
        assertEquals("타이틀", changedPost.getTitle());
        assertEquals("내용", changedPost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test5() {
        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("제목")
                .content("컨텐츠")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 입니다. id =" +  post.getId()));
        assertEquals("제목", changedPost.getTitle());
        assertEquals("컨텐츠", changedPost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test6() {
        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertEquals(0, postRepository.count());
    }


    @Test
    @DisplayName("글 1개 조회 - 존재하지 않는 글")
    void test7() {
        // given

        Post post = Post.builder()
                .title("제목")
                .content("컨텐츠")
                .build();
        postRepository.save(post);

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId() + 1L);
        });

    }

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void test8() {
        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);


        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1L);
        });

    }

    @Test
    @DisplayName("글 내용 수정 - 존재하지 않는 글")
    void test9() {
        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("제목")
                .content("컨텐츠")
                .build();

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId() + 1L, postEdit);
        });
    }
}