package net.testproj.auth.services;

import com.github.benmanes.caffeine.cache.Cache;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractOneTimeCodeService {
    protected static final Base64.Encoder B64 = Base64.getUrlEncoder().withoutPadding();
    protected static final SecureRandom RANDOM = new SecureRandom();
    protected final Cache<String, UUID> cache;
    protected final CodeHashService codeHashService;

    public abstract String issue(UUID userId);
    public abstract Optional<UUID> consume(String code);

    protected AbstractOneTimeCodeService(Cache<String, UUID> cache, CodeHashService codeHashService) {
        this.cache = cache;
        this.codeHashService = codeHashService;
    }
}
