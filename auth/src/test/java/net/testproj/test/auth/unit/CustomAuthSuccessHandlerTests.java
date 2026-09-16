package net.testproj.test.auth.unit;

import net.testproj.auth.handlers.CustomOAuth2SuccessHandler;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomAuthSuccessHandlerTests {

    private final CustomOAuth2SuccessHandler handler =
            new CustomOAuth2SuccessHandler(null,null,null,null);

    @Test
    void oAuth2AuthenticationTokenRequirement() {
        var  authentication = mock(Authentication.class);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> handler.onAuthenticationSuccess(
                        new MockHttpServletRequest(),
                        new MockHttpServletResponse(),
                        authentication
                )
        );
        assertEquals("Expected OAuth2AuthenticationToken but got: " + authentication.getClass(),
                exception.getMessage());
    }

    @Test
    void oidcUserRequirement() {
        var oAuth2AuthenticationToken = mock(OAuth2AuthenticationToken.class);
        when(oAuth2AuthenticationToken.getPrincipal()).thenReturn(mock(OAuth2User.class));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> handler.onAuthenticationSuccess(
                        new MockHttpServletRequest(),
                        new MockHttpServletResponse(),
                        oAuth2AuthenticationToken
                )
        );
        assertEquals("Expected OIDC user", exception.getMessage());
    }
}
