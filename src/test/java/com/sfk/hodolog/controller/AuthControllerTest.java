package com.sfk.hodolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfk.hodolog.domain.Users;
import com.sfk.hodolog.repository.PostRepository;
import com.sfk.hodolog.repository.SessionRepository;
import com.sfk.hodolog.repository.UserRepository;
import com.sfk.hodolog.request.Login;
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
}