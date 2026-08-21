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

//todo: правило ведения БД - никаких неявных установок значеник(дефолтов и т.д.)

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
        AuthUser authUser = AuthUser.builder()
                .email(requestDTO.getEmail().toLowerCase(Locale.ROOT))//todo: анализ , где именно делать toLowerCase
                .passwordHash(passwordEncoder.encode(requestDTO.getPassword()))
                .profileCompleted(Boolean.FALSE)
                .emailVerified(Boolean.FALSE).build();
        authUser = authUserDS.insert(authUser);

        emailSenderService.sendEmailVerificationCode(requestDTO.getEmail(),
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


    @PostMapping("/verify-email/resend")
    public void verifyEmailResend(@Valid @RequestBody Object todoRequestDTO) {
        //todo: much more later + rename method and endpoint-path

    }
}
