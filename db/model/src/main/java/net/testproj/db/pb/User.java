package net.testproj.db.pb;

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
public class User {
    private UUID id;
    private Instant dayOfBirth;
    private String nickName;
    private String providerAvatarUrl;
    private UUID authUserId;
}