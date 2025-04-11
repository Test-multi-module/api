package com.testproj.auth.security;

import com.testproj.auth.service.EmailService;
import com.testproj.db.auth.EmailConfirmationTokenDS;
import com.testproj.db.auth.schema.model.AuthUser;
import com.testproj.db.auth.AuthUserDS;
import com.testproj.db.auth.schema.model.EmailConfirmationToken;
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