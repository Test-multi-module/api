package net.testproj.auth.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "auth.security.oauth2-login")
public class OAuth2LoginProps {
    @NotBlank
    private String redirectUrl;

    @Valid
    @NotNull
    private OAuth2LoginProps.ExchangeCode exchangeCode;

    @Getter@Setter
    public static class ExchangeCode {
        @NotBlank
        private String pepper;

        @NotNull
        @Positive
        private Long ttlSeconds;
    }
}