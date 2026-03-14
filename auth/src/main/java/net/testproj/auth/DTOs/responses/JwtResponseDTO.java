package net.testproj.auth.DTOs.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter@Setter
public class JwtResponseDTO {
    private String token;
}