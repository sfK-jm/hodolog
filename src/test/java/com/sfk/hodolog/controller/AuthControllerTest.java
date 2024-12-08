package com.sfk.hodolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfk.hodolog.domain.Session;
import com.sfk.hodolog.domain.Users;
import com.sfk.hodolog.repository.PostRepository;
import com.sfk.hodolog.repository.SessionRepository;
import com.sfk.hodolog.repository.UserRepository;
import com.sfk.hodolog.request.Login;
import org.apache.catalina.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    public void test() throws Exception {
        //given
        userRepository.save(Users.builder()
                .name("test")
                .email("a@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("a@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // expected

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공후 세션1개 생성")
    public void test2() throws Exception {
        //given
        Users user = userRepository.save(Users.builder()
                .name("test")
                .email("a@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("a@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        Assertions.assertEquals(1L, user.getSessions().size());
    }


    @Test
    @DisplayName("로그인 성공후 세션 응답")
    public void test3() throws Exception {
        //given
        Users user = userRepository.save(Users.builder()
                .name("test")
                .email("a@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("a@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken", Matchers.notNullValue()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지 접속한다. /foo")
    public void test4() throws Exception {
        //given
        Users user = Users.builder()
                .name("test")
                .email("a@gmail.com")
                .password("1234")
                .build();

        Session session = user.addSession();

        userRepository.save(user);


        mockMvc.perform(MockMvcRequestBuilders.get("/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다.")
    public void test5() throws Exception {
        //given
        Users user = Users.builder()
                .name("test")
                .email("a@gmail.com")
                .password("1234")
                .build();

        Session session = user.addSession();

        userRepository.save(user);


        mockMvc.perform(MockMvcRequestBuilders.get("/foo")
                        .header("Authorization", session.getAccessToken() + "aa")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }
}