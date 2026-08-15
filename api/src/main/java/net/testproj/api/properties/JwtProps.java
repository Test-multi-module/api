package net.testproj.api.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Valid
    @NotNull
    private TrustStore truststore;

    @Getter@Setter
    public static class TrustStore {
        @NotBlank
        private String location;
        @NotBlank
        private String storePassword;
        @NotBlank
        private String type;
        @NotBlank
        private String alias;
    }

}