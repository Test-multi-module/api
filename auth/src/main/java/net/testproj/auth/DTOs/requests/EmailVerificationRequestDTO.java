package net.testproj.auth.DTOs.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import static net.testproj.auth.validation.AuthValidationPatterns.EMAIL_VERIFY_CODE_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationRequestDTO {
    @NotBlank(message = "Verification code must not be blank")
    @Pattern(regexp = EMAIL_VERIFY_CODE_PATTERN, message = "Verification code must contain exactly 6 digits")
    private String emailVerificationCode;

    @NotNull(message = "User ID must not be null")
    private UUID userId;

}
