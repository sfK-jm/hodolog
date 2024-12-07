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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
        String accessToken = authService.signin(login);
        return new SessionResponse(accessToken);
    }
}
