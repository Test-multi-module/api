package net.testproj.db.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser {

    private UUID id;
    private boolean profileCompleted;
    private String email;
    private String passwordHash;
    private boolean emailVerified;
}
