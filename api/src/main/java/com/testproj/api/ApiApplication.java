package com.testproj.api;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication(scanBasePackages = "com.testproj")
@Slf4j
public class ApiApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().directory("api/src/main/resources")
                .load();
        System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));
        System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
        SpringApplication app = new SpringApplication(ApiApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8080"));
       // String port = System.getenv("PORT");
       // if (port == null || port.isEmpty()) port = "8081";
       // app.setDefaultProperties(Collections.singletonMap("server.port", port));

        app.run(args);
    }
}
