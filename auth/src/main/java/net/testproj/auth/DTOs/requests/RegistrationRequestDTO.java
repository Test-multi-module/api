package net.testproj.auth.DTOs.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequestDTO {//todo validations for the fields + @JsonProperty

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    @Size(max = 254, message = "Email must be less than 254 characters")
    private String email;
    //todo: может как-то при маппинге с клиента через спринг-механизмы переводить в нижний регистр?

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    private String password;
}
