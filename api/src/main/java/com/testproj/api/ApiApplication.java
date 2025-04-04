package com.testproj.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication(scanBasePackages = "com.testproj")
@Slf4j
public class ApiApplication {
    public static void main(String[] args) {
        System.setProperty("MAIL_USERNAME", System.getenv("MAIL_USERNAME"));
        System.setProperty("MAIL_PASSWORD", System.getenv("MAIL_PASSWORD"));
        SpringApplication app = new SpringApplication(ApiApplication.class);
        String port = System.getenv("PORT");
        if (port == null || port.isEmpty()) port = "8081";
        app.setDefaultProperties(Collections.singletonMap("server.port", port));
        app.run(args);
    }
}
