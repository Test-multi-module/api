package net.testproj.test.auth.integration;

import com.github.benmanes.caffeine.cache.Cache;
import net.testproj.auth.DTOs.requests.ExchangeRequestDTO;
import net.testproj.auth.DTOs.responses.ExchangeResponseDTO;
import net.testproj.auth.properties.JwtProps;
import net.testproj.auth.services.LoginCodeService;
import net.testproj.test.auth.utils.TestAuthJwtConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.UUID;

@Import(TestAuthJwtConfig.class)
public class ExchangeControllerTests extends TestBase{
    @Autowired private LoginCodeService loginCodeService;
    @Autowired private JwtDecoder testJwtDecoder;
    @Autowired private JwtProps props;


    @Test
    void successfulExchangeOfOneTimeCode() throws Exception {
        UUID userID = UUID.randomUUID();
        String code = loginCodeService.issue(userID);

        var exchangeRequestDTO = ExchangeRequestDTO.builder().exchangeCode(code).build();
        var result = mockMvc.perform(post("/auth/exchange")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(exchangeRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();

        var responseBody = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ExchangeResponseDTO.class
        );

        Jwt jwt = testJwtDecoder.decode(responseBody.getAccessToken());
        assertNotNull(jwt.getId());
        assertEquals(userID.toString(), jwt.getSubject());
        assertEquals(props.getIssuer(), jwt.getClaimAsString("iss"));
        assertEquals(props.getAudience(), jwt.getAudience());
        assertEquals(
                Duration.ofSeconds(props.getAccessToken().getTtlSeconds()),
                Duration.between(jwt.getIssuedAt(), jwt.getExpiresAt())
        );
    }

    @Test
    void doubleExchangeOfOneTimeCode() throws Exception {
        UUID userID = UUID.randomUUID();
        String code = loginCodeService.issue(userID);

        var exchangeRequestDTO = ExchangeRequestDTO.builder().exchangeCode(code).build();
        mockMvc.perform(post("/auth/exchange")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(exchangeRequestDTO)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/auth/exchange")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(exchangeRequestDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void tryExchangeInvalidOneTimeCode() throws Exception {
        var exchangeRequestDTO = ExchangeRequestDTO.builder().exchangeCode(loginCodeService.generateCode()).build();
        mockMvc.perform(post("/auth/exchange")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(exchangeRequestDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void expiredOneTimeCodeIsUnauthorized() throws Exception {
        String code = loginCodeService.issue(UUID.randomUUID());
        Cache<?, ?> cache = (Cache<?, ?>) ReflectionTestUtils.getField(loginCodeService, "cache");
        assertNotNull(cache);
        var expiration = cache.policy().expireAfterWrite().orElseThrow();
        Duration originalTtl = expiration.getExpiresAfter();

        expiration.setExpiresAfter(Duration.ZERO);
        try {
            mockMvc.perform(post("/auth/exchange")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(
                                    ExchangeRequestDTO.builder().exchangeCode(code).build())))
                    .andExpect(status().isUnauthorized());
        } finally {
            expiration.setExpiresAfter(originalTtl);
        }
    }
}