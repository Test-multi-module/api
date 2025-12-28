package net.testproj.auth.handlers;

import net.testproj.auth.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.testproj.db.auth.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

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

        String providerUserId = String.valueOf(attrs.get("sub"));
        String email = String.valueOf(attrs.get("email"));
        String givenName = (String) attrs.get("given_name");
        String familyName = (String) attrs.get("family_name");
        String pictureUrl = (String) attrs.get("picture");

        OAuth2Account oAuth2Account = oAuth2AccountDS.getByProviderAndProviderUserId(providerUserId, provider);
        if (oAuth2Account == null) {

            User user = authUserDS.insert(User.builder().email(email).build());

            oAuth2Account = OAuth2Account.builder()
                    .userId(user.getId())
                    .provider(provider)
                    .providerUserId(providerUserId)
                    .emailAtProvider(email)
                    .providerAvatarUrl(pictureUrl)
                    .givenName(givenName)
                    .familyName(familyName)
                    .build();
            oAuth2AccountDS.insert(oAuth2Account);
        }

        //todo jwt generation
        UUID sub = oAuth2Account.getId();
        String jwt = jwtService.generateToken("todo generation and all process here");

        response.getWriter().write(jwt);
    }
}
