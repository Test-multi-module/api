package net.testproj.auth.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.testproj.auth.DTOs.requests.EmailVerificationRequestDTO;
import net.testproj.auth.DTOs.requests.RegistrationRequestDTO;
import net.testproj.auth.services.AuthRegisterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    private final AuthRegisterService authRegisterService;

    @PostMapping
    public void register(
            @Valid @RequestBody RegistrationRequestDTO requestDTO
    ) {
        authRegisterService.register(requestDTO);
    }

    @PostMapping("/verify-email")
    public void verifyEmail(
            @Valid @RequestBody EmailVerificationRequestDTO requestDTO
    ) {
        authRegisterService.verifyEmail(requestDTO);
    }
}
