package net.testproj.auth.services;

import com.github.benmanes.caffeine.cache.Caffeine;
import net.testproj.auth.properties.EmailVerificationProps;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailVerificationCodeService extends AbstractOneTimeCodeService{

    private final EmailVerificationProps emailVerificationProps;

    public EmailVerificationCodeService(EmailVerificationProps emailVerificationProps,
                                        CodeHashService codeHashService) {
        super(Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(emailVerificationProps.getTtlSeconds()))
                .maximumSize(200_000)
                .build(),
                codeHashService);
        this.emailVerificationProps = emailVerificationProps;
    }

    @Override
    public String issue(UUID userId) {
        String code = String.valueOf(100_000 + RANDOM.nextInt(900_000));
        String key = codeHashService.hashWithPepper(code, emailVerificationProps.getPepper());
        cache.put(key, userId);
        return code;
    }

    @Override
    public Optional<UUID> consume(String code) {
        String key = codeHashService.hashWithPepper(code, emailVerificationProps.getPepper());
        UUID userId = cache.asMap().remove(key);
        return Optional.ofNullable(userId);
    }
}
