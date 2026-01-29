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
    private final Cache<String, String> cache; // key = hash(code), value = userId

    public LoginCodeService(AuthProps authProps) {
        this.authProps = authProps;

        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(authProps.getLoginCodeTtlSeconds()))
                .maximumSize(200_000).build();
    }

    public String issue(String userId) {
        String code = generateUrlSafeToken(32); // 32 bytes = очень надёжно
        String key = hashWithPepper(code, authProps.getLoginCodePepper());
        cache.put(key, userId);
        return code;
    }

    public Optional<String> consume(String code) {
        String key = hashWithPepper(code, authProps.getLoginCodePepper());
        String userId = cache.asMap().remove(key);
        return Optional.ofNullable(userId);
    }

    private static String generateUrlSafeToken(int bytes) {
        byte[] buf = new byte[bytes];
        RANDOM.nextBytes(buf);
        return B64.encodeToString(buf);
    }

    private static String hashWithPepper(String code, String pepper) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");//возьми алгоритм SHA-256/результат всегда 32 байта/необратимый (из хэша нельзя восстановить вход)
            md.update(pepper.getBytes(StandardCharsets.UTF_8));
            md.update((byte) ':');//нужен что бы хеш ab+cd и a+bcd различались
            md.update(code.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();//это равно "сделай хеш"
            return B64.encodeToString(digest);//просто превратить поток байтов в строку
        } catch (Exception e) {
            throw new IllegalStateException("Cannot hash login code", e);
        }
    }
}
