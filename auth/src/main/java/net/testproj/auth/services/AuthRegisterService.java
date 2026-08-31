package net.testproj.auth.services;

import lombok.AllArgsConstructor;
import net.testproj.auth.DTOs.requests.EmailVerificationRequestDTO;
import net.testproj.auth.DTOs.requests.RegistrationRequestDTO;
import net.testproj.auth.DTOs.responses.RegistrationResponseDTO;
import net.testproj.auth.model.EmailVerificationIssueResult;
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

    public RegistrationResponseDTO register(RegistrationRequestDTO requestDTO){
        if(authUserDS.getByEmail(requestDTO.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        AuthUser authUser = AuthUser.builder()
                .email(requestDTO.getEmail())
                .passwordHash(passwordEncoder.encode(requestDTO.getPassword()))
                .profileCompleted(Boolean.FALSE)
                .emailVerified(Boolean.FALSE).build();
        authUser = authUserDS.insert(authUser);

        EmailVerificationIssueResult issued = emailVerificationCodeService.issue(authUser.getId());
        emailSenderService.sendEmailVerificationCode(authUser.getEmail(), issued.code());

        return RegistrationResponseDTO.builder().verificationId(issued.verificationId()).build();
    }

    public void verifyEmail(EmailVerificationRequestDTO dto) {
        UUID userId = emailVerificationCodeService
                .consume(dto.getEmailVerificationCode(), dto.getUserId());

        AuthUser authUser = authUserDS.getById(userId);
        authUser.setEmailVerified(Boolean.TRUE);
        authUserDS.update(authUser);
    }
}
