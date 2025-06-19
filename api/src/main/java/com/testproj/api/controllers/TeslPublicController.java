package com.testproj.api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeslPublicController {
    @GetMapping("/")
    public String home() {
        return "App is running!";
    }
}
