package net.testproj.auth.services;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class CodeHashService {
    private static final Base64.Encoder B64 = Base64.getUrlEncoder().withoutPadding();

    public String hashWithPepper(String verificationId, String pepper) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(pepper.getBytes(StandardCharsets.UTF_8));
            md.update((byte) ':');
            md.update(verificationId.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            return B64.encodeToString(digest);
        } catch (Exception e) {
            throw new IllegalStateException("Error during the hashing process", e);
        }
    }
}
