package net.testproj.test.auth.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.testproj.auth.DTOs.requests.LoginRequestDTO;
import net.testproj.auth.controllers.AuthSessionController;
import net.testproj.auth.services.JwtService;
import net.testproj.db.auth.AuthUserDS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthSessionController.class)
@ContextConfiguration(classes = WebTestApplication.class)
@ActiveProfiles("web-test")
class SessionControllerValidationTest {

    @Autowired private MockMvc mockMvc;
    @MockitoBean private JwtService jwtService;
    @MockitoBean private AuthUserDS authUserDS;
    @MockitoBean private PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @MethodSource("invalidLoginRequests")
    void invalidLoginRequestIsRejectedBeforeAuthentication(
            String email, String password, String field, String message) throws Exception {
        var result = mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new LoginRequestDTO(email, password))))
                .andExpect(status().isBadRequest())
                .andReturn();

        var error = assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException());
        assertTrue(error.getBindingResult().getFieldErrors(field).stream()
                .anyMatch(fieldError -> message.equals(fieldError.getDefaultMessage())));
        verifyNoInteractions(authUserDS, passwordEncoder, jwtService);
    }

    private static Stream<Arguments> invalidLoginRequests() {
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
}
