package net.testproj.test.auth.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.testproj.auth.DTOs.requests.RegistrationRequestDTO;
import net.testproj.auth.controllers.AuthRegisterController;
import net.testproj.auth.services.AuthRegisterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthRegisterController.class)
@ContextConfiguration(classes = WebTestApplication.class)
@ActiveProfiles("web-test")
@ExtendWith(OutputCaptureExtension.class)
public class AuthRegisterControllerValidationTests {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private AuthRegisterService authRegisterService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @MethodSource("invalidRegistrationRequests")
    void invalidRegistrationRequestIsRejectedBeforeServiceCall(
            String email, String password, String field, String message) throws Exception {
        var result = mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(
                                RegistrationRequestDTO.builder().email(email).password(password).build())))
                .andExpect(status().isBadRequest())
                .andReturn();

        var error = assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException());
        assertTrue(error.getBindingResult().getFieldErrors(field).stream()
                .anyMatch(fieldError -> message.equals(fieldError.getDefaultMessage())));
        verifyNoInteractions(authRegisterService);
    }

    private static Stream<Arguments> invalidRegistrationRequests() {
        return Stream.of(
                Arguments.of(null, "ValidPass!123", "email", "Email must not be blank"),
                Arguments.of(" ", "ValidPass!123", "email", "Email must not be blank"),
                Arguments.of("not-an-email", "ValidPass!123", "email", "Email must be valid"),
                Arguments.of("a".repeat(250) + "@x.com", "ValidPass!123", "email",
                        "Email must be less than 254 characters"),
                Arguments.of("user@example.com", null, "password", "Password must not be blank"),
                Arguments.of("user@example.com", " ", "password", "Password must not be blank"),
                Arguments.of("user@example.com", "short", "password",
                        "Password must be between 8 and 128 characters"),
                Arguments.of("user@example.com", "x".repeat(129), "password",
                        "Password must be between 8 and 128 characters")
        );
    }

    @Test
    void registrationWithInvalidDto(CapturedOutput output) throws Exception {
        RegistrationRequestDTO registrationRequestDTO = RegistrationRequestDTO.builder()
                .email("not-an-email")
                .password("badPw7!")
                .build();

        var result = mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registrationRequestDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
        var error = assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException());

        var emailError = error.getBindingResult().getFieldError("email");
        var passwordError = error.getBindingResult().getFieldError("password");
        assertNotNull(emailError, "Expected a validation error for the email field");
        assertNotNull(passwordError, "Expected a validation error for the password field");
        assertEquals("Email must be valid", emailError.getDefaultMessage());
        assertEquals("Password must be between 8 and 128 characters", passwordError.getDefaultMessage());
        verifyNoInteractions(authRegisterService);
        assertFalse(output.getAll().contains(registrationRequestDTO.getPassword()));
    }
}
