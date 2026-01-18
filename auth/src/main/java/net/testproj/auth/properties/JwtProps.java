package net.testproj.auth.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix="auth.security.jwt")
@Getter
@Setter
@Validated
public class JwtProps {
    private String issuer;
    @Valid
    private JwtProps.AccessTokenProps accessTokenProps;

    @Getter@Setter
    public static class AccessTokenProps {
        @NotBlank
        private String audience;

        @NotNull
        @Positive
        private Long ttlSeconds;
    }
}
