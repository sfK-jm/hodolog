package com.sfk.hodolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfk.hodolog.domain.Users;
import com.sfk.hodolog.repository.UserRepository;
import com.sfk.hodolog.request.Login;
import com.sfk.hodolog.request.Signup;
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

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    public void test6() throws Exception {
        //given
        Signup signup = Signup.builder()
                .email("a@gmail.com")
                .password("!234")
                .name("a")
                .build();


        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .content(objectMapper.writeValueAsString(signup))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}