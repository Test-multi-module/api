package com.testproj.auth.security.service;

import com.testproj.db.auth.schema.model.AuthUser;
import com.testproj.db.pb.schema.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthUserService implements UserDetailsService {
    private final JwtService jwtService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Логика загрузки пользователя из базы данных
        // Предполагается, что у тебя есть сущность AuthUser, которая представляет пользователя
       // AuthUser user = Optional.ofNullable(authUserDS.findByUsername(username)).orElseThrow(() -> new UsernameNotFoundException("AuthUser not found"));
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("mypass");

        AuthUser authUser = new AuthUser(UUID.randomUUID(), new Date(), new Date(), false, "mylogin",
                "ROLE_TODO", "mynickname", encodedPassword, "test@gmail.com");

        // Возвращаем пользователя, преобразуя его в объект UserDetails
        return new org.springframework.security.core.userdetails.User(
                authUser.getNickName(), authUser.getPassword(), Collections.emptyList());
    }
}

