package net.testproj.db.auth;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2Account {

    private UUID userId;
    private UUID id;

    private boolean deleted;
    private boolean disabled;

    private String provider;
    private String providerUserId;
    private String emailAtProvider;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastLoginAt;

    private String providerAvatarUrl;
    private String givenName;
    private String familyName;
}
