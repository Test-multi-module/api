package net.testproj.test.auth.integration;

import com.github.benmanes.caffeine.cache.Cache;
import net.testproj.auth.DTOs.requests.EmailVerificationRequestDTO;
import net.testproj.auth.DTOs.requests.RegistrationRequestDTO;
import net.testproj.auth.DTOs.responses.RegistrationResponseDTO;
import net.testproj.auth.services.EmailSenderService;
import net.testproj.auth.services.EmailVerificationCodeService;
import net.testproj.auth.properties.EmailVerificationProps;
import net.testproj.db.auth.AuthUser;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.Locale;
import java.util.UUID;

import static net.testproj.auth.validation.AuthValidationPatterns.EMAIL_VERIFY_CODE_PATTERN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegistrationControllerTests extends TestBase {
    //todo:  shoud be added rate-limit on issue/resend as issue email-verif-code set attempts to 0
    // TODO: New verification code invalidates previous ones; return a clear "invalid or expired code" message.

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EmailVerificationCodeService emailVerificationCodeService;
    @Autowired private EmailVerificationProps emailVerificationProps;
    @MockitoBean private EmailSenderService emailSenderService;

    @Test
    void registrationCreatesUnverifiedUserAndSendsCode() throws Exception {
        String email = uniqueEmail();
        String password = "ValidPass!123";

        var result = mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(
                                RegistrationRequestDTO.builder().email(email).password(password).build())))
                .andExpect(status().isOk())
                .andReturn();

        RegistrationResponseDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(), RegistrationResponseDTO.class);
        AuthUser user = authUserDS.getByEmail(email);
        assertNotNull(user);
        assertEquals(user.getId(), response.getUserId());
        assertFalse(user.getEmailVerified());
        assertNotNull(user.getPasswordHash());
        assertTrue(passwordEncoder.matches(password, user.getPasswordHash()));

        var codeCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailSenderService).sendEmailVerificationCode(eq(email), codeCaptor.capture());
        assertTrue(codeCaptor.getValue().matches(EMAIL_VERIFY_CODE_PATTERN));
    }

    @Test
    void duplicateEmailIsRejectedIgnoringCaseWithoutSendingAnotherCode() throws Exception {
        AuthUser existingUser = insertUnverifiedUser();

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(RegistrationRequestDTO.builder()
                                .email(existingUser.getEmail().toUpperCase(Locale.ROOT))
                                .password("AnotherPass!123")
                                .build())))
                .andExpect(status().isBadRequest());

        assertEquals(existingUser.getId(), authUserDS.getByEmail(existingUser.getEmail()).getId());
        verifyNoInteractions(emailSenderService);
    }

    @Test
    void validCodeVerifiesEmail() throws Exception {
        AuthUser user = insertUnverifiedUser();
        String code = emailVerificationCodeService.issue(user.getId()).code();
        verifyEmail(user.getId(), code, 200);
        assertTrue(authUserDS.getById(user.getId()).getEmailVerified());
    }

    @Test
    void incorrectCodeIsUnauthorizedAndLeavesEmailUnverified() throws Exception {
        AuthUser user = insertUnverifiedUser();
        emailVerificationCodeService.issue(user.getId());
        verifyEmail(user.getId(), "000000", 401);
        assertFalse(authUserDS.getById(user.getId()).getEmailVerified());
    }

    @Test
    void tooManyIncorrectCodesInvalidateTheIssuedCode() throws Exception {
        AuthUser user = insertUnverifiedUser();
        String code = emailVerificationCodeService.issue(user.getId()).code();

        for (int attempt = 0; attempt < emailVerificationProps.getAttempts(); attempt++) {
            verifyEmail(user.getId(), "000000", 401);
        }
        verifyEmail(user.getId(), code, 401);

        assertFalse(authUserDS.getById(user.getId()).getEmailVerified());
    }

    @Test
    void codeIssuedForAnotherUserCannotVerifyEmail() throws Exception {
        AuthUser codeOwner = insertUnverifiedUser();
        AuthUser otherUser = insertUnverifiedUser();
        String code = emailVerificationCodeService.issue(codeOwner.getId()).code();
        verifyEmail(otherUser.getId(), code, 401);

        assertFalse(authUserDS.getById(codeOwner.getId()).getEmailVerified());
        assertFalse(authUserDS.getById(otherUser.getId()).getEmailVerified());
    }

    @Test
    void verificationCodeCannotBeUsedTwice() throws Exception {
        AuthUser user = insertUnverifiedUser();
        String code = emailVerificationCodeService.issue(user.getId()).code();

        verifyEmail(user.getId(), code, 200);
        verifyEmail(user.getId(), code, 401);
    }

    @Test
    void expiredCodeIsUnauthorizedAndLeavesEmailUnverified() throws Exception {
        AuthUser user = insertUnverifiedUser();
        String code = emailVerificationCodeService.issue(user.getId()).code();

        Cache<?, ?> cache = (Cache<?, ?>) ReflectionTestUtils.getField(emailVerificationCodeService, "cache");
        assertNotNull(cache);
        var expiration = cache.policy().expireAfterWrite().orElseThrow();
        Duration originalTtl = expiration.getExpiresAfter();

        expiration.setExpiresAfter(Duration.ZERO);
        try {
            verifyEmail(user.getId(), code, 401);
        } finally {
            expiration.setExpiresAfter(originalTtl);
        }

        assertFalse(authUserDS.getById(user.getId()).getEmailVerified());
    }

    private AuthUser insertUnverifiedUser() {
        return authUserDS.insert(AuthUser.builder()
                .email(uniqueEmail())
                .passwordHash(passwordEncoder.encode("ValidPass!123"))
                .emailVerified(false)
                .build());
    }

    private String uniqueEmail() {
        return "registration-" + UUID.randomUUID() + "@example.com";
    }

    private void verifyEmail(UUID userId, String code, int expectedStatus) throws Exception {
        mockMvc.perform(post("/auth/register/verify-email")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(
                                new EmailVerificationRequestDTO(code, userId))))
                .andExpect(status().is(expectedStatus));
    }
}