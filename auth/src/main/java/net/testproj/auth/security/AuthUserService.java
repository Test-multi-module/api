package net.testproj.auth.security;

import lombok.AllArgsConstructor;
import net.testproj.db.auth.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
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
        User user = new User(UUID.randomUUID(),
                Instant.now(), Instant.now(), null,null, "test@gmail.com");

        // Возвращаем пользователя, преобразуя его в объект UserDetails
        return new org.springframework.security.core.userdetails.User(
                user.getId().toString(), Integer.toString(user.getId().version()), Collections.emptyList());
    }
}

