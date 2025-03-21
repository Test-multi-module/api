package com.testproj.api.config;


import com.testproj.auth.security.service.AuthUserService;
import com.testproj.auth.security.service.JwtAuthenticationFilter;
import com.testproj.auth.security.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@AllArgsConstructor
@EnableWebSecurity//Включает поддержку Spring Security. Если её не указать, то Spring Security не будет работать.
@EnableGlobalMethodSecurity(prePostEnabled = true)//Разрешает аннотации уровня метода, такие как @PreAuthorize("hasRole('ADMIN')") и т.д
public class SecurityConfig {

    private final AuthUserService authUserService;
    private final JwtService jwtService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService, authUserService);
    }
    @Bean//настройка http безопасности для auth-сервера
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/public/**").permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/logout").permitAll());

        return http.build();
    }

    @Bean public PasswordEncoder passwordEncoder() {
        // Обязательный?-Да, если используешь UserDetailsService | Указывает, что пароли должны кодироваться с помощью BCrypt
        // когда пользователь регистрируется или меняет свой пароль, пароль будет зашифрован c использованием алгоритма bcrypt,
        // чтобы он не сохранялся в базе данных в открытом виде. при попытке входа в систему, введённый пользователем пароль
        // будет зашифрован и проверен против уже зашифрованного пароля в базе данных.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        /* Создаёт менеджер аутентификации, который использует мой кастомный сервис AuthUserService для загрузки пользователей.
        Устанавливает кодировщик паролей BCryptPasswordEncoder.Обязательный метод, ибо у меня кастомная аутентификация.
        Использовала бы стандартную, можно было бы его не добавлять */
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(this.authUserService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}