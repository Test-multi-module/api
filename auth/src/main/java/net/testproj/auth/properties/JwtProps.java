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

    @NotBlank
    private String issuer;
    @Valid
    @NotNull
    private JwtProps.AccessTokenProps accessToken;

    @Valid
    @NotNull
    private JwtProps.KeystoreProps keystore;

    @Getter@Setter
    public static class AccessTokenProps {
        @NotBlank
        private String audience;

        @NotNull
        @Positive
        private Long ttlSeconds;
    }

    @Getter@Setter
    public static class KeystoreProps {
        @NotBlank private String location;
        @NotBlank private String storePassword;
        @NotBlank private String keyAlias;
        @NotBlank private String keyPassword;
        @NotBlank private String type;
    }
}