package net.testproj.auth.controllers;

import lombok.AllArgsConstructor;
import net.testproj.auth.DTOs.requests.ExchangeRequestDTO;
import net.testproj.auth.DTOs.responses.ExchangeResponseDTO;
import net.testproj.auth.services.JwtService;
import net.testproj.auth.services.LoginCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@RestController
@RequestMapping("/auth/exchange")
@AllArgsConstructor
public class AuthExchangeController {

    private final LoginCodeService loginCodeService;
    private final JwtService jwtService;

    @PostMapping
    public ExchangeResponseDTO exchange(@RequestBody ExchangeRequestDTO request) {

        String userId = loginCodeService.consume(request.getCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired code"));

        String accessToken = jwtService.issueAccessToken(userId, Collections.emptyList());

        return new ExchangeResponseDTO(accessToken);
    }
}
