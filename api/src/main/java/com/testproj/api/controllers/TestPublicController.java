package com.testproj.api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestPublicController {
    @GetMapping()
    public String home() {
        return "App is running!";
    }
}
