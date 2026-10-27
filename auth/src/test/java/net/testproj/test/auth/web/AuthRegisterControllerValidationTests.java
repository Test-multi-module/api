package net.testproj.test.auth.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.testproj.auth.DTOs.requests.RegistrationRequestDTO;
import net.testproj.auth.controllers.AuthRegisterController;
import net.testproj.auth.services.AuthRegisterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthRegisterController.class)
@ContextConfiguration(classes = WebTestApplication.class)
@ActiveProfiles("web-test")
@ExtendWith(OutputCaptureExtension.class)
public class AuthRegisterControllerValidationTests {
    @Autowired protected MockMvc mockMvc;
    protected final ObjectMapper objectMapper = new ObjectMapper();
    @MockitoBean
    private AuthRegisterService authRegisterService;

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
