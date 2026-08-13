package net.testproj.auth.controllers;

import lombok.AllArgsConstructor;
import net.testproj.auth.DTOs.requests.EmailVerificationRequestDTO;
import net.testproj.auth.DTOs.requests.RegistrationRequestDTO;
import net.testproj.auth.services.LoginCodeService;
import net.testproj.db.auth.AuthUser;
import net.testproj.db.auth.AuthUserDS;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//todo: правило ведения БД - никаких неявных установок значеник(дефолтов и т.д.)

@RestController
@RequestMapping("/auth/register")
@AllArgsConstructor
public class AuthRegisterController {
    private final LoginCodeService loginCodeService;
    private final AuthUserDS authUserDS;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public void register(@RequestBody RegistrationRequestDTO requestDTO) {
        AuthUser authUser = AuthUser.builder()
                .email(requestDTO.getEmail())
                .passwordHash(passwordEncoder.encode(requestDTO.getPassword()))
                .profileCompleted(Boolean.FALSE)
                .emailVerified(Boolean.FALSE).build();
        authUser = authUserDS.insert(authUser);
        //todo: send code to the email
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
