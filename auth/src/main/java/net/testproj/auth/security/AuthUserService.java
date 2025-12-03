package net.testproj.auth.security;

import lombok.AllArgsConstructor;
import net.testproj.db.auth.schema.model.AuthUser;
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
    @Override//todo: all logic extracting user
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // Логика загрузки пользователя из базы данных
        // Предполагается, что у тебя есть сущность AuthUser, которая представляет пользователя
       // AuthUser user = Optional.ofNullable(authUserDS.findByUsername(userId)).orElseThrow(() -> new UsernameNotFoundException("AuthUser not found"));
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("mypass");
        //UUID.fromString("550e8400-e29b-41d4-a716-446655440000")//from postman
        AuthUser authUser = new AuthUser(UUID.randomUUID(),
                new Date(), new Date(), false,false, "mylogin",
                0, encodedPassword, "test@gmail.com");

        // Возвращаем пользователя, преобразуя его в объект UserDetails
        return new org.springframework.security.core.userdetails.User(
                authUser.getLogin(), authUser.getPassword(), Collections.emptyList());
    }
}

