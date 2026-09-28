package net.testproj.test.auth;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {
        "net.testproj.auth",
        "net.testproj.common",
        "net.testproj.db",
        "net.testproj.test.auth.utils"
})
@ConfigurationPropertiesScan(basePackages = "net.testproj.auth.properties")
public class AuthTestApplication {
}
