package net.testproj.auth.security;

import net.testproj.auth.service.EmailService;
import net.testproj.db.auth.EmailConfirmationTokenDS;
import net.testproj.db.auth.schema.model.AuthUser;
import net.testproj.db.auth.AuthUserDS;
import net.testproj.db.auth.schema.model.EmailConfirmationToken;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RegisterUserService {

    private final AuthUserDS authUserDS;
    private final EmailService emailService;
    private EmailConfirmationTokenDS emailConfirmationTokenDS;

    public AuthUser register(AuthUser authUser){
        try {
            //todo : pay attention | ERROR: insert or update on table "email_confirmation_tokens" violates foreign key constraint "email_confirmation_tokens_user_id_fkey" ()
            //todo: entirely logic could be planned and built
            authUser = authUserDS.create(authUser);
            EmailConfirmationToken emailConfirmationToken = emailConfirmationTokenDS.create(authUser.getId());

            emailService.sendConfirmationEmail(authUser.getEmail(), emailConfirmationToken.getToken());
        }
        catch (MessagingException e) {

        }
        return authUser;
    }

    public void activate(UUID registrationToken){

    }
}