package net.testproj.test.auth;

import net.testproj.auth.DTOs.requests.EmailVerificationRequestDTO;
import net.testproj.auth.DTOs.requests.RegistrationRequestDTO;
import net.testproj.auth.DTOs.responses.RegistrationResponseDTO;
import net.testproj.auth.services.EmailSenderService;
import net.testproj.auth.services.EmailVerificationCodeService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static net.testproj.auth.validation.AuthValidationPatterns.EMAIL_VERIFY_CODE_PATTERN;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegistrationFlowTests extends TestBase {
    //todo:  shoud be added rate-limit on issue/resend as issue email-verif-code set attempts to 0
    // TODO: New verification code invalidates previous ones; return a clear "invalid or expired code" message.

    private final String testMail = "test@example.com";
    private final String testPassword = "Pass!123";
    @Autowired private PasswordEncoder passwordEncoder;
    @MockitoBean private EmailSenderService emailSenderService;
    @MockitoSpyBean private EmailVerificationCodeService emailVerificationCodeService;

    @Test
    void successfulRegistrationAndDoubleRegisterAttempt() throws Exception {
        RegistrationRequestDTO registrationRequestDTO = RegistrationRequestDTO.builder()
                .email(testMail)
                .password(testPassword)
                .build();

        var result = mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();

        var user = authUserDS.getByEmail(testMail);
        assertFalse(user.getEmailVerified());
        assertNotNull(user.getPasswordHash());
        assertTrue(passwordEncoder.matches(testPassword, user.getPasswordHash()));

        var responseBody =
                objectMapper.readValue(result.getResponse().getContentAsString(), RegistrationResponseDTO.class);

        ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailSenderService).sendEmailVerificationCode(eq(testMail), codeCaptor.capture());
        String code = codeCaptor.getValue();
        assertTrue(code.matches(EMAIL_VERIFY_CODE_PATTERN));

        EmailVerificationRequestDTO verificationRequestDTO =
                new EmailVerificationRequestDTO(code, responseBody.getUserId());

        mockMvc.perform(post("/auth/register/verify-email")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(verificationRequestDTO)))
                .andExpect(status().isOk());
        user = authUserDS.getByEmail(testMail);
        assertTrue(user.getEmailVerified());

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationRequestDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(emailVerificationCodeService, times(1)).issue(any());
        verify(emailSenderService, times(1)).sendEmailVerificationCode(any(), any());
    }

    @Test
    void registrationWithInvalidDto() throws Exception {
        RegistrationRequestDTO registrationRequestDTO = RegistrationRequestDTO.builder()
                .email("not-an-email")
                .password("short")
                .build();

        var result = mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationRequestDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        //todo: check error messages

        verify(emailVerificationCodeService, never()).issue(any());
        verify(emailSenderService, never()).sendEmailVerificationCode(any(), any());
    }
}