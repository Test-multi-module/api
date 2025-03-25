package com.testproj.api.controllers;

import com.testproj.api.dtos.models.UserDTO;
import com.testproj.api.services.UserService;
import com.testproj.auth.models.LoginRequest;
import com.testproj.api.dtos.models.RegisterRequestDTO;
import com.testproj.db.auth.schema.model.AuthUser;
import com.testproj.auth.security.service.RegisterUserService;
import com.testproj.auth.models.JwtResponse;
import com.testproj.auth.security.service.JwtService;
import io.beanmapper.BeanMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;//todo should be injected to AuthUserService
    private final RegisterUserService registerUserService;
    private final UserService userService;
    private final BeanMapper beanMapper;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserId(),
                    loginRequest.getPassword()));
             String token = jwtService.generateToken(loginRequest.getUserId());
            return ResponseEntity.ok(new JwtResponse(token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


    @PostMapping("/register")
    //todo maybe separate converter for extracting AuthUser,UserDTO OR @RequestBody SomeContainerWithUserAndAuthUserTypes request
    public ResponseEntity<JwtResponse> register(@RequestBody RegisterRequestDTO request) {
        AuthUser authUser = registerUserService.register(beanMapper.map(request, AuthUser.class));
        userService.create(beanMapper.map(request,UserDTO.class));
        String token = jwtService.generateToken(authUser.getId());
        return ResponseEntity.ok(new JwtResponse(token));
    }
}