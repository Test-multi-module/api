package net.testproj.test.auth.integration;

import net.testproj.auth.handlers.CustomOAuth2SuccessHandler;
import net.testproj.auth.properties.OAuth2LoginProps;
import net.testproj.auth.services.LoginCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CustomOAuth2SuccessHandlerTests extends TestBase {
    @Autowired private CustomOAuth2SuccessHandler handler;
    @Autowired private OAuth2LoginProps props;
    @Autowired private LoginCodeService loginCodeService;

    private static final String sub = UUID.randomUUID().toString();
    private static final String provider = "google";
    private static final String email = "test@example.com";
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

    @Test
    void unverifiedProviderEmailIsRejectedWithoutCreatingUser() {
        String subject = UUID.randomUUID().toString();
        String unverifiedEmail = "unverified-" + UUID.randomUUID() + "@example.com";
        MockHttpServletResponse response = new MockHttpServletResponse();

        OAuth2AuthenticationException error = assertThrows(OAuth2AuthenticationException.class,
                () -> handler.onAuthenticationSuccess(
                        new MockHttpServletRequest(), response,
                        getOAuth2AuthenticationToken(subject, unverifiedEmail, false, "Test", "User")));

        assertEquals("Email not verified", error.getError().getErrorCode());
        assertNull(authUserDS.getByEmail(unverifiedEmail));
        assertNull(oAuth2AccountDS.getByProviderUserIdAndProvider(subject, provider));
        assertNull(response.getRedirectedUrl());
    }

    @Test
    void existingProviderAccountIsReusedAndUpdated() throws IOException {
        String subject = UUID.randomUUID().toString();
        String accountEmail = "oauth-" + UUID.randomUUID() + "@example.com";

        handler.onAuthenticationSuccess(new MockHttpServletRequest(), new MockHttpServletResponse(),
                getOAuth2AuthenticationToken(subject, accountEmail, true, "First", "Name"));
        var originalAccount = oAuth2AccountDS.getByProviderUserIdAndProvider(subject, provider);
        assertNotNull(originalAccount);

        MockHttpServletResponse response = new MockHttpServletResponse();
        handler.onAuthenticationSuccess(new MockHttpServletRequest(), response,
                getOAuth2AuthenticationToken(subject, accountEmail, true, "Updated", "Surname"));

        var updatedAccount = oAuth2AccountDS.getByProviderUserIdAndProvider(subject, provider);
        assertEquals(originalAccount.getId(), updatedAccount.getId());
        assertEquals(originalAccount.getUserId(), updatedAccount.getUserId());
        assertEquals("Updated", updatedAccount.getGivenName());
        assertEquals("Surname", updatedAccount.getFamilyName());
        assertEquals(302, response.getStatus());
        String code = UriComponentsBuilder.fromUriString(response.getRedirectedUrl())
                .build().getQueryParams().getFirst("code");
        assertNotNull(code);
        assertEquals(originalAccount.getUserId(), loginCodeService.consume(code));
    }

    private static OAuth2AuthenticationToken getOAuth2AuthenticationToken() {
        return getOAuth2AuthenticationToken(sub, email, true, givenName, familyName);
    }

    private static OAuth2AuthenticationToken getOAuth2AuthenticationToken(
            String subject, String accountEmail, boolean emailVerified, String firstName, String lastName) {
        Instant now = Instant.now();

        Map<String, Object> idTokenClaims = Map.of(
                "sub", subject, "email", accountEmail, "email_verified", emailVerified);

        Map<String, Object> userInfoClaims =
                Map.of("sub", subject, "name", firstName + " " + lastName,
                        "given_name", firstName, "family_name", lastName);

        OidcIdToken idToken =
                new OidcIdToken("fake-id-token", now, now.plusSeconds(3600), idTokenClaims);

        OidcUserInfo userInfo = new OidcUserInfo(userInfoClaims);

        OidcUser oidcUser = new DefaultOidcUser(
                List.of(new OidcUserAuthority(idToken, userInfo)), idToken, userInfo, "sub");

        return new OAuth2AuthenticationToken(oidcUser, oidcUser.getAuthorities(), provider);
    }
}