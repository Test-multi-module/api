package com.testproj.auth.security.service;

import com.testproj.db.auth.AuthUserDS;
import com.testproj.db.model.Role;
import com.testproj.db.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthUserService implements UserDetailsService {
    private final AuthUserDS authUserDS;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Логика загрузки пользователя из базы данных
        // Предполагается, что у тебя есть сущность User, которая представляет пользователя
       // User user = Optional.ofNullable(authUserDS.findByUsername(username)).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("mypass");

        User user = new User(1L,"myname",encodedPassword);

        // Возвращаем пользователя, преобразуя его в объект UserDetails
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(),
                Collections.emptyList());
    }
}

