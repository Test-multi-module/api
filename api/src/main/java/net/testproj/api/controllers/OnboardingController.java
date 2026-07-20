package net.testproj.api.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.testproj.api.DTOs.CreateProfileRequestDTO;
import net.testproj.api.DTOs.UserDTO;
import net.testproj.api.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/api/private/onboarding")
@AllArgsConstructor//todo swagger-documentation (whole project)
public class OnboardingController {

    private final UserService userService;

    @PostMapping("/create-profile")
    public UserDTO createProfile(
            @Valid @RequestBody CreateProfileRequestDTO dto,
            @AuthenticationPrincipal Jwt jwt) throws Exception {//todo: AdviceController?
        return userService.createProfile(jwt, dto);
    }
}