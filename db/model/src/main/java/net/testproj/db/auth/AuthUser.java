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
public class AuthUser {//todo analise if smth like notNull annotations needed here to show that field is not null in db

    private UUID id;
    private String email;
    private String passwordHash;
    private Boolean emailVerified;
}
