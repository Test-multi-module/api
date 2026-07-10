package net.testproj.auth.services;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.testproj.auth.properties.AuthProps;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;

@Service
public class LoginCodeService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Base64.Encoder B64 = Base64.getUrlEncoder().withoutPadding();

    private final AuthProps authProps;
    private final Cache<String, String> cache;

    public LoginCodeService(AuthProps authProps) {
        this.authProps = authProps;

        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(authProps.getLoginCodeTtlSeconds()))
                .maximumSize(200_000).build();
    }

    public String issue(String userId) {
        String code = generateUrlSafeToken();
        String key = hashWithPepper(code, authProps.getLoginCodePepper());
        cache.put(key, userId);
        return code;
    }

    public Optional<String> consume(String code) {
        String key = hashWithPepper(code, authProps.getLoginCodePepper());
        String userId = cache.asMap().remove(key);
        return Optional.ofNullable(userId);
    }

    private static String generateUrlSafeToken() {
        byte[] buf = new byte[32];
        RANDOM.nextBytes(buf);
        return B64.encodeToString(buf);
    }

    private static String hashWithPepper(String code, String pepper) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(pepper.getBytes(StandardCharsets.UTF_8));
            md.update((byte) ':');
            md.update(code.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            return B64.encodeToString(digest);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot hash login code", e);
        }
    }
}
