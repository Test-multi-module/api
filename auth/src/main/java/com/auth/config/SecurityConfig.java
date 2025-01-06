package com.auth.config;


import com.auth.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }
    @Bean//настройка http безопасности для auth-сервера
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/public/**").permitAll() // открытый доступ к публичным API
                        .anyRequest().authenticated() // требовать авторизацию для остальных эндпоинтов
                )
                .formLogin(form -> form
                        .loginPage("/login") // указание кастомной страницы логина
                        .permitAll() // разрешить доступ всем к странице логина
                        .loginProcessingUrl("/login/authenticate") // URL для обработки логина
                        .usernameParameter("username") // имя параметра для поля username
                        .passwordParameter("password") // имя параметра для поля password
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL для выхода
                        .permitAll() // разрешить всем выходить
                );

        return http.build();
    }

    @Bean public PasswordEncoder passwordEncoder() {
        //BCryptPasswordEncoder будет использоваться для:
        // Шифрования паролей: когда пользователь регистрируется или меняет свой пароль, пароль будет зашифрован с
        // использованием алгоритма bcrypt, чтобы он не сохранялся в базе данных в открытом виде.
        // Проверки паролей: при попытке входа в систему, введённый пользователем пароль будет зашифрован и проверен
        // против уже зашифрованного пароля в базе данных.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(this.customUserDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}