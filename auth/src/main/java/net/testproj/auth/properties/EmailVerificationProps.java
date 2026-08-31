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
@ConfigurationProperties(prefix = "auth.security.email-verification.code")
public class EmailVerificationProps {
    @NotNull
    @Positive
    private Long ttlSeconds;

    @NotBlank
    private String pepper;

    @NotNull
    private Integer attempts;
}
