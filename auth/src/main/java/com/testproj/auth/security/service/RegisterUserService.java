package com.testproj.auth.security.service;

import com.testproj.db.auth.schema.model.AuthUser;
import com.testproj.db.auth.AuthUserDS;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterUserService {
    private final AuthUserDS authUserDS;
    public AuthUser register(AuthUser authUser){
        authUser = authUserDS.create(authUser);
        return authUser;
    }
}