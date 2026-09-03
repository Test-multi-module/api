package net.testproj.api.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix="api.security.jwt")
@Getter @Setter
@Validated
public class JwtProps {
    @NotBlank
    private String issuer;

    @NotBlank
    private String audience;

    @NotBlank
    private String jwkSetUri;
}