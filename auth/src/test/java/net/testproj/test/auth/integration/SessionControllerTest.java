package net.testproj.test.auth.integration;

import net.testproj.auth.DTOs.requests.LoginRequestDTO;
import net.testproj.auth.DTOs.responses.LoginResponseDTO;
import net.testproj.auth.properties.JwtProps;
import net.testproj.db.auth.AuthUser;
import net.testproj.test.auth.utils.TestAuthJwtConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestAuthJwtConfig.class)
class SessionControllerTest extends TestBase {

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtDecoder testJwtDecoder;
    @Autowired private JwtProps props;

    @Test
    void verifiedUserCanLoginAndReceivesAccessToken() throws Exception {
        String password = "ValidPass!123";
        AuthUser user = insertUser(password, true);

        var result = mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new LoginRequestDTO(user.getEmail(), password))))
                .andExpect(status().isOk())
                .andReturn();

        LoginResponseDTO responseBody = objectMapper.readValue(
                result.getResponse().getContentAsString(), LoginResponseDTO.class);
        assertNotNull(responseBody.getAccessToken());

        var userID = user.getId();

        Jwt jwt = testJwtDecoder.decode(responseBody.getAccessToken());
        assertNotNull(jwt.getId());
        assertEquals(userID.toString(), jwt.getSubject());
        assertEquals(props.getIssuer(), jwt.getClaimAsString("iss"));
        assertEquals(props.getAudience(), jwt.getAudience());
        assertEquals(
                Duration.ofSeconds(props.getAccessToken().getTtlSeconds()),
                Duration.between(jwt.getIssuedAt(), jwt.getExpiresAt()));
    }

    @Test
    void wrongPasswordIsUnauthorized() throws Exception {
        AuthUser user = insertUser("ValidPass!123", true);
        assertLoginStatus(user.getEmail(), "WrongPass!123", 401);
    }

    @Test
    void unknownEmailIsUnauthorized() throws Exception {
        assertLoginStatus(uniqueEmail(), "ValidPass!123", 401);
    }

    @Test
    void userWithoutPasswordIsUnauthorized() throws Exception {
        AuthUser user = insertUser(null, true);
        assertLoginStatus(user.getEmail(), "ValidPass!123", 401);
    }

    @Test
    void unverifiedEmailIsForbidden() throws Exception {
        String password = "ValidPass!123";
        AuthUser user = insertUser(password, false);
        assertLoginStatus(user.getEmail(), password, 403);
    }

    private AuthUser insertUser(String password, boolean emailVerified) {
        return authUserDS.insert(AuthUser.builder()
                .email(uniqueEmail())
                .passwordHash(password == null ? null : passwordEncoder.encode(password))
                .emailVerified(emailVerified)
                .build());
    }

    private String uniqueEmail() {
        return "email-" + UUID.randomUUID() + "@example.com";
    }

    private void assertLoginStatus(String email, String password, int expectedStatus) throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new LoginRequestDTO(email, password))))
                .andExpect(status().is(expectedStatus));
    }
}
