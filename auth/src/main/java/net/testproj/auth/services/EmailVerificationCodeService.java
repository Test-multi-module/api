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
import java.util.UUID;

//todo: нужен общий абстрактный класс-шаблонные-методы и тд для EmailVerificationCodeService и LoginCodeService
@Service
public class EmailVerificationCodeService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Base64.Encoder B64 = Base64.getUrlEncoder().withoutPadding();


    private final AuthProps authProps;
    private final Cache<String, UUID> cache;

    public EmailVerificationCodeService(AuthProps authProps) {
        this.authProps = authProps;

        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(authProps.getEmailVerificationCodeTtlSeconds()))
                .maximumSize(200_000).build();
    }

    public String issue(UUID userId) {
        String code = generateCode();
        String key = hashWithPepper(code, authProps.getEmailVerificationCodePepper());
        cache.put(key, userId);
        return code;
    }

    public Optional<UUID> consume(String code) {
        String key = hashWithPepper(code, authProps.getEmailVerificationCodePepper());
        UUID userId = cache.asMap().remove(key);
        return Optional.ofNullable(userId);
    }

    private static String generateCode() {
        //todo анализ, почему разный алг генерации с LoginCodeService.generateCode
        return String.valueOf(100_000 + RANDOM.nextInt(900_000));
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
            throw new IllegalStateException("Cannot hash email verification code", e);
        }
    }
}
