package net.testproj.test.auth;

import net.testproj.auth.handlers.CustomOAuth2SuccessHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpSession;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OAuth2FlowTests extends TestBase {

    @MockitoSpyBean
    private CustomOAuth2SuccessHandler successHandler;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Test
    void googleOAuth2ClientRegistrationUsesTestProperties() {
        var googleRegistration = clientRegistrationRepository.findByRegistrationId("google");
        assertNotNull(googleRegistration);
        assertEquals("test-client-id", googleRegistration.getClientId());
        assertEquals("test-client-secret", googleRegistration.getClientSecret());
    }

    @Test
    void googleOAuth2AuthorizationEndpointRedirectsToGoogle() throws Exception {
        mockMvc.perform(get("/oauth2/authorization/google"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.LOCATION, containsString("accounts.google.com")))
                .andExpect(header().string(HttpHeaders.LOCATION, containsString("response_type=code")))
                .andExpect(header().string(HttpHeaders.LOCATION, not(containsString("test-client-secret"))))
                .andExpect(header().string(HttpHeaders.LOCATION, containsString("client_id=test-client-id")))
                .andExpect(header().string(HttpHeaders.LOCATION, containsString("redirect_uri=")))
                .andExpect(header().string(HttpHeaders.LOCATION, containsString("state=")));
    }

    @Test
    void googleOAuth2CallbackWithWrongStateDoesNotCallSuccessHandler() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/login/oauth2/code/google")
                        .session(session)
                        .param("code", "fake-code")
                        .param("state", "wrong-state"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string(HttpHeaders.LOCATION, not(containsString("/auth/callback"))));

        verify(successHandler, never()).onAuthenticationSuccess(
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any()
        );
    }
}
