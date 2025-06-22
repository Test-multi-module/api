package net.testproj.api.controllers;

import net.testproj.auth.models.LoginRequest;
import net.testproj.api.dtos.models.RegisterRequestDTO;
import net.testproj.db.auth.schema.model.AuthUser;
import net.testproj.auth.security.RegisterUserService;
import net.testproj.auth.models.JwtResponse;
import net.testproj.auth.security.JwtService;
import io.beanmapper.BeanMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;//todo should be injected to AuthUserService
    private final RegisterUserService registerUserService;
    private final BeanMapper beanMapper;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUserid().toString(), loginRequest.getPassword()));
             String token = jwtService.generateToken(loginRequest.getUserid().toString());
            return ResponseEntity.ok(new JwtResponse(token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/confirm-email/{token}")
    public  ResponseEntity<Void> confirmEmail(
            @PathVariable("token") UUID token) {
        registerUserService.activate(token);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequestDTO request) {
        registerUserService.register(beanMapper.map(request, AuthUser.class));
        return ResponseEntity.ok().build();
    }
}