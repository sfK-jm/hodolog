package com.sfk.hodolog.controller;

import com.sfk.hodolog.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PostController {

    @PostMapping("/posts")
    public String get(@RequestBody PostCreate params) {
        log.info("params={}", params);
        return "Hello world!";
    }
}
