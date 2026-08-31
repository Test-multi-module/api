package net.testproj.auth.DTOs.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationRequestDTO {
    //todo validations for the fields + @JsonProperty
    private String emailVerificationCode;

    private UUID userId;

}
