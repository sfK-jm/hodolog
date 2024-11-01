package com.sfk.hodolog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HodologApplication {

    public static void main(String[] args) {
        SpringApplication.run(HodologApplication.class, args);
        System.out.println("{\"title\": \"제목입니다\", \"content\": \"내용입니다.\"}");
    }

}
