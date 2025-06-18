package com.sfk.hodolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfk.hodolog.config.HodologMockUser;
import com.sfk.hodolog.domain.Post;
import com.sfk.hodolog.domain.Users;
import com.sfk.hodolog.repository.post.PostRepository;
import com.sfk.hodolog.repository.UserRepository;
import com.sfk.hodolog.request.post.PostCreate;
import com.sfk.hodolog.request.post.PostEdit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @HodologMockUser
    @DisplayName("글 작성 요청시 title과 content를 출력")
    void test() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();


        String json = objectMapper.writeValueAsString(request);

        System.out.println("json = " + json);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청시 title값은 필수다")
    void test2() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @HodologMockUser
    @DisplayName("글 작성")
    void test3() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        // given
        Post post = Post.builder()
                .title("123456789012345")
                .content("컨텐츠")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("123456789012345"))
                .andExpect(jsonPath("$.content").value("컨텐츠"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("글의 타이틀의 길이가 10미만일 경우")
    void test4_1() throws Exception {
        // given
        Post post = Post.builder()
                .title("12345")
                .content("컨텐츠")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("12345"))
                .andExpect(jsonPath("$.content").value("컨텐츠"))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @HodologMockUser
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        // given
        Users user = userRepository.findAll().get(0);

        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("타이틀 - " + i)
                        .content("컨텐츠 - " + i)
                        .user(user)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=1&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()", Matchers.is(10)))
                .andExpect(jsonPath("$.items[0].title").value("타이틀 - 30"))
                .andExpect(jsonPath("$.items[0].content").value("컨텐츠 - 30"))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @HodologMockUser
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    void test6() throws Exception {
        // given
        Users user = userRepository.findAll().get(0);

        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("타이틀 - " + i)
                        .content("컨텐츠 - " + i)
                        .user(user)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size", Matchers.is(10)))
                .andExpect(jsonPath("$.items[0].title").value("타이틀 - 30"))
                .andExpect(jsonPath("$.items[0].content").value("컨텐츠 - 30"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @HodologMockUser
    @DisplayName("글 제목수정")
    void test7() throws Exception {
        // given
        Users user = userRepository.findAll().get(0);

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .user(user)
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("타이틀")
                .content("내용")
                .build();

        // expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @HodologMockUser
    @DisplayName("게시글 삭제")
    void test8() throws Exception {
        // given
        Users user = userRepository.findAll().get(0);

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .user(user)
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "a@gmail.com", roles = {"ADMIN"})
    @DisplayName("존재하지 않는 게시글 수정")
    void test10() throws Exception {
        //given
        PostEdit postEdit = PostEdit.builder()
                .title("타이틀")
                .content("내용")
                .build();

        //expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
}