package net.testproj.auth.model;

import java.util.UUID;

public record EmailVerificationIssueResult(
    UUID userId,
    String code
) {}