package net.testproj.auth.properties;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@ConfigurationProperties(prefix = "api.security.cors")
@Getter
@Setter
@Validated
public class CorsProps {
    @NotEmpty private List<String> allowedOrigins;
}
