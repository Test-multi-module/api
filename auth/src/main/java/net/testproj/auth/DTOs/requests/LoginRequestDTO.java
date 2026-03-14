package net.testproj.auth.DTOs.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
public class LoginRequestDTO {
    private UUID userid;
    private String password;
}
