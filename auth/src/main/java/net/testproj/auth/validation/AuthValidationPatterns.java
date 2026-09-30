package net.testproj.auth.validation;

public final class AuthValidationPatterns {
    public static final String EMAIL_VERIFY_CODE_PATTERN = "\\d{6}";
    public static final String EXCHANGE_CODE_PATTERN = "^[A-Za-z0-9_-]{43}$";
}
