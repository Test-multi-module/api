package net.testproj.auth.services;

import lombok.AllArgsConstructor;
import net.testproj.auth.DTOs.requests.EmailVerificationRequestDTO;
import net.testproj.auth.DTOs.requests.RegistrationRequestDTO;
import net.testproj.db.auth.AuthUser;
import net.testproj.db.auth.AuthUserDS;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthRegisterService {
    private final AuthUserDS authUserDS;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;
    private final EmailVerificationCodeService emailVerificationCodeService;

    public void register(RegistrationRequestDTO requestDTO){
        if(authUserDS.getByEmail(requestDTO.getEmail()) != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        AuthUser authUser = AuthUser.builder()
                .email(requestDTO.getEmail())
                .passwordHash(passwordEncoder.encode(requestDTO.getPassword()))
                .profileCompleted(Boolean.FALSE)
                .emailVerified(Boolean.FALSE).build();
        authUser = authUserDS.insert(authUser);

        emailSenderService.sendEmailVerificationCode(authUser.getEmail(),
                emailVerificationCodeService.issue(authUser.getId()));
    }

    public void verifyEmail(EmailVerificationRequestDTO requestDTO){
        UUID userId = emailVerificationCodeService
                .consume(requestDTO.getEmailVerificationCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired code"));

        AuthUser authUser = authUserDS.getById(userId);
        authUser.setEmailVerified(Boolean.TRUE);
        authUserDS.update(authUser);
    }
}
