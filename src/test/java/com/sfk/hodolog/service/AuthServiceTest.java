package com.sfk.hodolog.service;

import com.sfk.hodolog.crypto.PasswordEncoder;
import com.sfk.hodolog.domain.Users;
import com.sfk.hodolog.exception.AlreadyExistsEmailException;
import com.sfk.hodolog.exception.InvalidSigninInformation;
import com.sfk.hodolog.repository.UserRepository;
import com.sfk.hodolog.request.Login;
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
        assertNotEquals("1234", user.getPassword());
        assertNotNull(user.getPassword());
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

    @Test
    @DisplayName("로그인 성공")
    void test3() {
        // given
        PasswordEncoder encoder = new PasswordEncoder();
        String encryptedPassword = encoder.encrypt("1234");

        Users user = Users.builder()
                .email("a@gmail.com")
                .password(encryptedPassword)
                .name("a")
                .build();

        userRepository.save(user);

        Login login = Login.builder()
                .email("a@gmail.com")
                .password("1234")
                .build();

        // when
        Long userId = authService.signin(login);

        // then
        assertNotNull(userId);
    }


    @Test
    @DisplayName("로그인시 비밀번호 틀림")
    void test4() {
        // given
        PasswordEncoder encoder = new PasswordEncoder();
        String encryptedPassword = encoder.encrypt("1234");

        Users user = Users.builder()
                .email("a@gmail.com")
                .password(encryptedPassword)
                .name("a")
                .build();

        userRepository.save(user);

        Login login = Login.builder()
                .email("a@gmail.com")
                .password("12345")
                .build();

        // expected
        assertThrows(InvalidSigninInformation.class, () -> authService.signin(login));

    }
}