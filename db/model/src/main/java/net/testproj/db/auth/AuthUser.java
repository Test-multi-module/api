package net.testproj.db.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser {

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    private boolean disabled;
    private boolean deleted;

    private String email;
    private int authorizationType;
}
