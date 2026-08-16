package net.testproj.auth.services;

import com.github.benmanes.caffeine.cache.Caffeine;
import net.testproj.auth.properties.OAuth2LoginProps;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoginCodeService extends AbstractOneTimeCodeService{

    private final OAuth2LoginProps oAuth2LoginProps;

    public LoginCodeService(CodeHashService codeHashService,
                            OAuth2LoginProps oAuth2LoginProps) {
        super(Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(oAuth2LoginProps.getExchangeCode().getTtlSeconds()))
                .maximumSize(200_000)
                .build(),
                codeHashService);
        this.oAuth2LoginProps = oAuth2LoginProps;
    }

    @Override
    public String issue(UUID userId) {
        byte[] buf = new byte[32];
        RANDOM.nextBytes(buf);
        String code = B64.encodeToString(buf);
        String key = codeHashService.hashWithPepper(code, oAuth2LoginProps.getExchangeCode().getPepper());
        cache.put(key, userId);
        return code;
    }

    @Override
    public Optional<UUID> consume(String code) {
        String key = codeHashService.hashWithPepper(code, oAuth2LoginProps.getExchangeCode().getPepper());
        UUID userId = cache.asMap().remove(key);
        return Optional.ofNullable(userId);
    }
}
