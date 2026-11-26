package net.testproj.test.auth.integration;

import net.testproj.auth.DTOs.requests.EmailVerificationRequestDTO;
import net.testproj.auth.DTOs.requests.LoginRequestDTO;
import net.testproj.auth.DTOs.requests.RegistrationRequestDTO;
import net.testproj.auth.DTOs.responses.LoginResponseDTO;
import net.testproj.auth.DTOs.responses.RegistrationResponseDTO;
import net.testproj.auth.services.EmailSenderService;
import net.testproj.auth.services.EmailVerificationCodeService;
import net.testproj.test.auth.utils.TestAuthJwtConfig;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.UUID;

import static net.testproj.auth.validation.AuthValidationPatterns.EMAIL_VERIFY_CODE_PATTERN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestAuthJwtConfig.class)
public class UserOnboardingFlowTest extends TestBase {

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtDecoder testJwtDecoder;
    @MockitoBean private EmailSenderService emailSenderService;
    @MockitoSpyBean private EmailVerificationCodeService emailVerificationCodeService;

    @Test
    void registrationVerificationAndLoginFlow() throws Exception {
        String testPassword = "Pass!123";
        String testMail = "onboarding-" + UUID.randomUUID() + "@example.com";
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

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(testMail, testPassword);
        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isForbidden());

        EmailVerificationRequestDTO verificationRequestDTO =
                new EmailVerificationRequestDTO(code, responseBody.getUserId());

        mockMvc.perform(post("/auth/register/verify-email")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(verificationRequestDTO)))
                .andExpect(status().isOk());
        user = authUserDS.getByEmail(testMail);
        assertTrue(user.getEmailVerified());

        var loginResult = mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();
        LoginResponseDTO loginResponse = objectMapper.readValue(
                loginResult.getResponse().getContentAsString(), LoginResponseDTO.class);
        assertNotNull(loginResponse.getAccessToken());
        assertEquals(user.getId().toString(), testJwtDecoder.decode(loginResponse.getAccessToken()).getSubject());

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationRequestDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(emailVerificationCodeService, times(1)).issue(any());
        verify(emailSenderService, times(1)).sendEmailVerificationCode(any(), any());
    }
}
