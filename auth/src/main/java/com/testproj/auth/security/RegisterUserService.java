package com.testproj.auth.security;

import com.testproj.auth.service.EmailService;
import com.testproj.db.auth.schema.model.AuthUser;
import com.testproj.db.auth.AuthUserDS;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterUserService {
    private final AuthUserDS authUserDS;
    private final EmailService emailService;
    public AuthUser register(AuthUser authUser){
        authUser = authUserDS.create(authUser);
        try {//todo
            emailService.sendConfirmationEmail(authUser.getEmail());
        } catch (MessagingException e){//todo
        }
        return authUser;
    }
}