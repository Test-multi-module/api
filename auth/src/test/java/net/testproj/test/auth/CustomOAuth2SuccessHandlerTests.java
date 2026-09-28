package net.testproj.test.auth;

import net.testproj.auth.handlers.CustomOAuth2SuccessHandler;
import net.testproj.auth.properties.OAuth2LoginProps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CustomOAuth2SuccessHandlerTests extends TestBase {
    @Autowired private CustomOAuth2SuccessHandler handler;
    @Autowired private OAuth2LoginProps props;

    private static final String sub = UUID.randomUUID().toString();
    private static final String provider = "google";
    private static final String email = "test@example.com";
    private static final String name = "Test User";
    private static final String givenName = "Test";
    private static final String familyName = "User";

    @Test
    void successHandlerLogic() throws IOException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        handler.onAuthenticationSuccess(new MockHttpServletRequest(), response, getOAuth2AuthenticationToken());

        assertEquals(302, response.getStatus());
        assertNotNull(response.getRedirectedUrl());
        assertTrue(response.getRedirectedUrl().startsWith(props.getRedirectUrl().concat("?code=")));

        var user = authUserDS.getByEmail(email);
        assertTrue(user.getEmailVerified());
        assertNull(user.getPasswordHash());

        var acc = oAuth2AccountDS.getByUserID(user.getId());
        assertNotNull(acc.getId());
        assertEquals(sub, acc.getProviderUserId());
        assertEquals(provider, acc.getProvider());
        assertEquals(email, acc.getEmailAtProvider());
        assertEquals(givenName, acc.getGivenName());
        assertEquals(familyName, acc.getFamilyName());
    }

    private static OAuth2AuthenticationToken getOAuth2AuthenticationToken() {
        Instant now = Instant.now();

        Map<String, Object> idTokenClaims = Map.of("sub", sub, "email", email, "email_verified", true);

        Map<String, Object> userInfoClaims =
                Map.of("sub", sub, "name", name, "given_name", givenName, "family_name", familyName);

        OidcIdToken idToken =
                new OidcIdToken("fake-id-token", now, now.plusSeconds(3600), idTokenClaims);

        OidcUserInfo userInfo = new OidcUserInfo(userInfoClaims);

        OidcUser oidcUser = new DefaultOidcUser(
                List.of(new OidcUserAuthority(idToken, userInfo)), idToken, userInfo, "sub");

        return new OAuth2AuthenticationToken(oidcUser, oidcUser.getAuthorities(), provider);
    }
}