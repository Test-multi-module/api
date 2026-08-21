package net.testproj.auth.DTOs.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequestDTO {//todo validations for the fields + @JsonProperty
    private String email;//todo: может как-то при маппинге с клиента через спринг-механизмы переводить в нижний регистр?
    private String password;
}
