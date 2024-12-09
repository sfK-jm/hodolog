package com.sfk.hodolog.controller;

import com.sfk.hodolog.domain.Users;
import com.sfk.hodolog.exception.InvalidRequest;
import com.sfk.hodolog.exception.InvalidSigninInformation;
import com.sfk.hodolog.repository.UserRepository;
import com.sfk.hodolog.request.Login;
import com.sfk.hodolog.response.SessionResponse;
import com.sfk.hodolog.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody Login login) {
        String accessToken = authService.signin(login);
        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
                .domain("localhost") // todo 서버 환경에 따른 분리 필요
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();


        log.info(">>>>>>>>>>>>>>>>> cookie={}", cookie.toString());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();

    }
}
