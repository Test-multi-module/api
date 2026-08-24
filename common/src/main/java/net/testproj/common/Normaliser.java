package net.testproj.common;

import java.util.Locale;
import java.util.Objects;

public class Normaliser {
    public static String normalizeEmail(String email) {
        Objects.requireNonNull(email, "email must not be null");
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
