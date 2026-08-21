package net.testproj.auth.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.testproj.auth.DTOs.requests.LoginRequestDTO;
import net.testproj.auth.DTOs.responses.LoginResponseDTO;
import net.testproj.auth.services.JwtService;
import net.testproj.db.auth.AuthUser;
import net.testproj.db.auth.AuthUserDS;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Locale;

@RestController
@RequestMapping("/auth/login")
@AllArgsConstructor
public class AuthLoginController {
    private final JwtService jwtService;
    private final AuthUserDS authUserDS;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        AuthUser user = authUserDS.getByEmail(loginRequestDTO.getEmail().toLowerCase(Locale.ROOT));
        //todo: null-user case (пока не уверенна как это будет , мб вообще буду рассчитвать на enf из ds)
        //todo: ResponseStatusException - тоже пока не уверенна, как именно у меня будут выбрасываться ексепшны (из каких либ, буду ли создавать свои спец обьекты и тд)
        if (user == null || user.getPasswordHash() == null ||
                !passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email is not verified");
        }

        String accessToken = jwtService.issueAccessToken(user.getId(), Collections.emptyList());
        return new LoginResponseDTO(accessToken);
    }
    //todo: подумать, не хочу ли я хеши в отдельныю таблицу вынести
}
