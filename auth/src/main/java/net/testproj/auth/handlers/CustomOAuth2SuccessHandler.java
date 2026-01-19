package net.testproj.auth.handlers;

import net.testproj.auth.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.testproj.db.auth.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;

@Component
@AllArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    private final OAuth2AccountDS oAuth2AccountDS;
    private final AuthUserDS authUserDS;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        if (!(authentication instanceof OAuth2AuthenticationToken token)) {
            throw new IllegalStateException("Expected OAuth2AuthenticationToken but got: " + authentication.getClass());
        }

        String provider = token.getAuthorizedClientRegistrationId();

        OAuth2User oAuth2User = token.getPrincipal();
        Map<String, Object> attrs = oAuth2User.getAttributes();

        if (!(oAuth2User instanceof OidcUser oidcUser)) {
            throw new IllegalStateException("Expected OIDC user");
        }

        String providerUserId = String.valueOf(attrs.get("sub"));
        String email = String.valueOf(attrs.get("email"));
        boolean emailVerified = Boolean.TRUE.equals(oidcUser.getEmailVerified());
        String familyName = (String) attrs.get("family_name");
        String givenName = (String) attrs.get("given_name");
        String pictureUrl = (String) attrs.get("picture");

        OAuth2Account oAuth2Account = oAuth2AccountDS.getByProviderUserIdAndProvider(providerUserId, provider);
        if (oAuth2Account != null) {
            oAuth2AccountDS.update(oAuth2Account.getId(), email, emailVerified, Instant.now(),
                    pictureUrl, givenName, familyName);
            authUserDS.update(oAuth2Account.getUserId(), email);
        } else {

            User user = authUserDS.insert(User.builder().email(email).build());

            oAuth2Account = OAuth2Account.builder()
                    .userId(user.getId())
                    .provider(provider)
                    .providerUserId(providerUserId)
                    .emailAtProvider(email)
                    .providerAvatarUrl(pictureUrl)
                    .givenName(givenName)
                    .familyName(familyName)
                    .emailVerified(emailVerified)
                    .build();
            oAuth2AccountDS.insert(oAuth2Account);
        }

        String jwt = jwtService.issueAccessToken(oAuth2Account.getUserId().toString(), new ArrayList<>());

        response.getWriter().write(jwt);
    }
}