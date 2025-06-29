package com.sfk.hodolog.controller;

import com.sfk.hodolog.config.AppConfig;
import com.sfk.hodolog.request.Signup;
import com.sfk.hodolog.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;

    @PostMapping("/api/auth/signup")
    public void signup(@RequestBody Signup signup) {
        authService.signup(signup);
    }
}
