package net.testproj.auth.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.testproj.auth.properties.AuthProps;
import net.testproj.auth.services.LoginCodeService;
import net.testproj.db.auth.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Component
@AllArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final LoginCodeService loginCodeService;

    private final OAuth2AccountDS oAuth2AccountDS;
    private final AuthUserDS authUserDS;
    private final AuthProps authProps;


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

        String loginCode = loginCodeService.issue(oAuth2Account.getUserId().toString());

        String redirectUrl = UriComponentsBuilder
                .fromUriString(authProps.getLoginRedirectUrl())
                .queryParam("code", loginCode)
                .build(true)//говорит Spring’у:«НЕ трогай и НЕ перекодируй уже готовые части URL».
                .toUriString();

        response.sendRedirect(redirectUrl);//cам ставит 302 статус и locationHeader
        //todo анализ можно ли как-то протестить что и куда я редирекчу , бо какая-то игра в воду
    }
}