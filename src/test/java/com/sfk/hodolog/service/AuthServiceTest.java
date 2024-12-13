package com.sfk.hodolog.service;

import com.sfk.hodolog.domain.Users;
import com.sfk.hodolog.exception.AlreadyExistsEmailException;
import com.sfk.hodolog.repository.UserRepository;
import com.sfk.hodolog.request.Signup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        // given
        Signup signup = Signup.builder()
                .password("1234")
                .email("a@gmail.com")
                .name("test")
                .build();

        // when
        authService.signup(signup);

        // then
        assertEquals(1, userRepository.count());

        Users user = userRepository.findAll().iterator().next();
        assertEquals("a@gmail.com", user.getEmail());
        assertEquals("1234", user.getPassword());
        assertEquals("test", user.getName());

    }

    @Test
    @DisplayName("회원가입시 중복된 이메일")
    void test2() {
        // given
        Users user = Users.builder()
                .email("a@gmail.com")
                .password("1234")
                .name("a")
                .build();

        userRepository.save(user);

        Signup signup = Signup.builder()
                .password("1234")
                .email("a@gmail.com")
                .name("test")
                .build();

        // expected
        assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signup));

    }
}