package net.testproj.auth.DTOs.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static net.testproj.auth.validation.AuthValidationPatterns.EXCHANGE_CODE_PATTERN;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRequestDTO {
    @NotBlank
    @Pattern(regexp = EXCHANGE_CODE_PATTERN)
    private String exchangeCode;
}
