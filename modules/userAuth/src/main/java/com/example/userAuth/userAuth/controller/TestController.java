package com.example.userAuth.userAuth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")  // <-- root URL
    public String home() {
        return "Hello, authenticated user!";
    }

    @GetMapping("/hello")  // optional additional endpoint
    public String hello() {
        return "Hello world!";
    }
}
