package net.testproj.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmailVerificationCacheEntry {

    @Builder.Default
    private Integer failedAttempts = 0;

    private String hashedCode;
}
