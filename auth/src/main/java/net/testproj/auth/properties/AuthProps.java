package net.testproj.auth.properties;

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
@ConfigurationProperties(prefix = "auth")
public class AuthProps {

    @NotBlank
    private String loginRedirectUrl;

    @NotNull
    @Positive
    private Long loginCodeTtlSeconds;

    @NotBlank
    private String loginCodePepper;
}
