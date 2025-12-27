package net.testproj.db.auth;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.UUID;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthorizationType {

    private UUID userId;
    private UUID id;

    private boolean deleted;
    private boolean disabled;

    private Instant createdAt;
    private Instant updatedAt;

    private int authorizationType;
}
