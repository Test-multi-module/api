package net.testproj.test.auth;

import net.testproj.auth.handlers.CustomOAuth2SuccessHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class JwksEndpointTests extends TestBase {
    @Autowired private CustomOAuth2SuccessHandler handler;


    @Autowired
    private MockMvc mockMvc;

    @Test
    void jwksEndpoint() throws Exception {
        mockMvc.perform(get("/auth/.well-known/jwks.json"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CACHE_CONTROL, containsString("max-age=3600")))
                .andExpect(header().string(HttpHeaders.CACHE_CONTROL, containsString("public")))
                .andExpect(jsonPath("$.keys").isArray())
                .andExpect(jsonPath("$.keys[0].kid").exists())
                .andExpect(content().string(not(containsString("\"d\""))))
                .andExpect(content().string(not(containsString("\"p\""))))
                .andExpect(content().string(not(containsString("\"q\""))))
                .andExpect(content().string(not(containsString("\"dp\""))))
                .andExpect(content().string(not(containsString("\"dq\""))))
                .andExpect(content().string(not(containsString("\"qi\""))));
    }
}