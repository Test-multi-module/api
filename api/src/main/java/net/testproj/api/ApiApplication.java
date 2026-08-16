package net.testproj.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.Collections;

@SpringBootApplication(scanBasePackages = "net.testproj")
@ConfigurationPropertiesScan(basePackages = "net.testproj")
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ApiApplication.class);
        String port = System.getenv("PORT");
        if (port == null || port.isEmpty()) port = "8081";
        app.setDefaultProperties(Collections.singletonMap("server.port", port));
        app.run(args);
    }
}
