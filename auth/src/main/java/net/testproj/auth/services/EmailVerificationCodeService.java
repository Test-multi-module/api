package net.testproj.auth.services;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import net.testproj.auth.model.EmailVerificationCacheEntry;
import net.testproj.auth.model.EmailVerificationIssueResult;
import net.testproj.auth.properties.EmailVerificationProps;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

//todo: подход по обработке исключений(и логированию)
//todo: подготовить инфру для тестов, что бы хорошо проверить процесс регистрации в разных сценариях (ретрофит?)

@Service
@Validated
public class EmailVerificationCodeService {

    protected static final SecureRandom RANDOM = new SecureRandom();
    protected final CodeHashService codeHashService;
    private final EmailVerificationProps emailVerificationProps;
    protected final Cache<UUID, EmailVerificationCacheEntry> cache;


    public EmailVerificationCodeService(EmailVerificationProps emailVerificationProps, CodeHashService codeHashService) {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(emailVerificationProps.getTtlSeconds()))
                .maximumSize(200_000)
                .build();
        this.emailVerificationProps = emailVerificationProps;
        this.codeHashService = codeHashService;
    }

    public EmailVerificationIssueResult issue(@NotNull UUID userId) {
        String code = String.valueOf(100_000 + RANDOM.nextInt(900_000));
        String hashedCode = codeHashService.hashWithPepper(code, emailVerificationProps.getPepper());
        cache.put(userId, EmailVerificationCacheEntry.builder().hashedCode(hashedCode).failedAttempts(0).build());
        return new EmailVerificationIssueResult(userId, code);
    }

    public UUID consume(@NotNull @Pattern(regexp = "\\d{6}") String code,
                        @NotNull UUID userId) {
        String hashedCode = codeHashService.hashWithPepper(code, emailVerificationProps.getPepper());
        AtomicBoolean verified = new AtomicBoolean(false);

        cache.asMap().computeIfPresent(userId, (key, entry) -> {
            if(!Objects.equals(hashedCode, entry.getHashedCode())) {
                int failedAttempts = entry.getFailedAttempts() + 1;
                entry.setFailedAttempts(failedAttempts);
                return failedAttempts >= emailVerificationProps.getAttempts() ? null : entry;
            }
            verified.set(true);
            return null;
        });

        if (!verified.get()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "//todo");
        }
        return userId;
    }
}
