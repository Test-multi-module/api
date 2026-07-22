package net.testproj.auth.controllers;

import lombok.AllArgsConstructor;
import net.testproj.auth.DTOs.requests.EmailVerificationRequestDTO;
import net.testproj.auth.DTOs.requests.RegistrationRequestDTO;
import net.testproj.auth.services.LoginCodeService;
import net.testproj.db.auth.AuthUser;
import net.testproj.db.auth.AuthUserDS;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/register")
@AllArgsConstructor
public class AuthRegisterController {
    private final LoginCodeService loginCodeService;
    private final AuthUserDS authUserDS;

    @PostMapping
    public void register(@RequestBody RegistrationRequestDTO requestDTO) {
        PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16, 32, 1, 65536, 3);
        String hash = passwordEncoder.encode(requestDTO.getPassword());

        AuthUser authUser = AuthUser.builder()
                .email(requestDTO.getEmail())
                .passwordHash(hash)
                .profileCompleted(Boolean.FALSE)
                .emailVerified(Boolean.FALSE).build();
        authUser = authUserDS.insert(authUser);
        //todo: add user email not verified, send code to the email
        //todo after analysis before - some response obj
    }

    @PostMapping("/verify-email")
    public void verifyEmail(@RequestBody EmailVerificationRequestDTO requestDTO) {
        //todo: set email verified for user, generate and return login-code(or better redirect to client???)
        //todo after analysis before - some response obj
    }


    @PostMapping("/verify-email/resend")
    public void verifyEmailResend(@RequestBody Object todoRequestDTO) {
        //todo: much more later + rename method and endpoint-path

    }
}
