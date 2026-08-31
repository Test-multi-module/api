package net.testproj.auth.services;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.testproj.auth.properties.OAuth2LoginProps;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;

@Service
public class LoginCodeService  {

    protected static final Base64.Encoder B64 = Base64.getUrlEncoder().withoutPadding();
    protected static final SecureRandom RANDOM = new SecureRandom();
    protected final CodeHashService codeHashService;

    private final OAuth2LoginProps oAuth2LoginProps;
    protected final Cache<String, UUID> cache;

    public LoginCodeService(CodeHashService codeHashService, OAuth2LoginProps oAuth2LoginProps) {
        this.codeHashService = codeHashService;
        cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(oAuth2LoginProps.getExchangeCode().getTtlSeconds()))
                .maximumSize(200_000)
                .build();
        this.oAuth2LoginProps = oAuth2LoginProps;
    }


    public String issue(UUID userId) {
        byte[] buf = new byte[32];
        RANDOM.nextBytes(buf);
        String code = B64.encodeToString(buf);
        String key = codeHashService.hashWithPepper(code, oAuth2LoginProps.getExchangeCode().getPepper());
        cache.put(key, userId);
        return code;
    }

    public UUID consume(String code) {
        String key = codeHashService.hashWithPepper(code, oAuth2LoginProps.getExchangeCode().getPepper());
        UUID userId = cache.asMap().remove(key);
        if (userId == null){
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, "//todo");
        }
        return userId;
    }
}
