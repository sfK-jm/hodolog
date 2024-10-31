package com.sfk.hodolog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {
    @GetMapping("/post")
    public String get() {
        return "Hello world!";
    }
}
