package net.testproj.auth.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.testproj.auth.DTOs.requests.EmailVerificationRequestDTO;
import net.testproj.auth.DTOs.requests.RegistrationRequestDTO;
import net.testproj.auth.services.EmailSenderService;
import net.testproj.auth.services.EmailVerificationCodeService;
import net.testproj.db.auth.AuthUser;
import net.testproj.db.auth.AuthUserDS;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.UUID;

//todo: НИКОГДА НЕ ЛОГИРОВАТЬ ПАРОЛЬ
//todo: правило ведения БД - никаких неявных установок значеник(дефолтов и т.д.)
//todo:  где-то еще должен біть change password
//todo: Api&Auth ControllerAdvice classes
//todo: much more later + rename method and endpoint-path: /verify-email/resend
//todo swagger-documentation (whole project)

@RestController
@RequestMapping("/auth/register")
@AllArgsConstructor
public class AuthRegisterController {
    private final AuthUserDS authUserDS;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;
    private final EmailVerificationCodeService emailVerificationCodeService;

    @PostMapping
    public void register(@Valid @RequestBody RegistrationRequestDTO requestDTO) {
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

    @PostMapping("/verify-email")
    public void verifyEmail(@Valid @RequestBody EmailVerificationRequestDTO requestDTO) {
        UUID userId = emailVerificationCodeService.consume(requestDTO.getEmailVerificationCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired code"));

        AuthUser authUser = authUserDS.getById(userId);
        authUser.setEmailVerified(Boolean.TRUE);
        authUserDS.update(authUser);
    }
}
