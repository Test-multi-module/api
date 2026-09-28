package net.testproj.auth.DTOs.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRequestDTO {
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9_-]{43}$")
    private String exchangeCode;
}
