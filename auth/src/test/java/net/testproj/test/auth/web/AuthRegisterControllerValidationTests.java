package net.testproj.test.auth.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.testproj.auth.DTOs.requests.RegistrationRequestDTO;
import net.testproj.auth.controllers.AuthRegisterController;
import net.testproj.auth.services.AuthRegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthRegisterController.class)
@ContextConfiguration(classes = WebTestApplication.class)
@ActiveProfiles("web-test")
public class AuthRegisterControllerValidationTests {
    @Autowired protected MockMvc mockMvc;
    protected final ObjectMapper objectMapper = new ObjectMapper();
    @MockitoBean
    private AuthRegisterService authRegisterService;

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
    }
}
