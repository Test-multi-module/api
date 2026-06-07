package net.testproj.tests.integ;

import net.testproj.auth.handlers.CustomOAuth2SuccessHandler;
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

class ApiApplicationTests extends IntegTestBase {
    @Autowired private CustomOAuth2SuccessHandler handler;

    @Test
    void successHandlerLogic() throws IOException {
        Map<String, Object> claims = Map.of(
                "sub", "123",
                "email", "test@example.com",
                "name", "Test User"
        );
        Instant now = Instant.now();
        OAuth2AuthenticationToken auth = getOAuth2AuthenticationToken(now, claims);

        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();

        handler.onAuthenticationSuccess(request, response, auth);
    }

    private static OAuth2AuthenticationToken getOAuth2AuthenticationToken(Instant now, Map<String, Object> claims) {
        OidcIdToken idToken =
                new OidcIdToken("fake-id-token", now, now.plusSeconds(3600), claims);

        OidcUserInfo userInfo = new OidcUserInfo(claims);

        OidcUser oAuth2User = new DefaultOidcUser(
                List.of(new OidcUserAuthority(idToken, userInfo)),
                idToken, userInfo, "sub");

        return new OAuth2AuthenticationToken(
                oAuth2User, oAuth2User.getAuthorities(), "google");
    }
}