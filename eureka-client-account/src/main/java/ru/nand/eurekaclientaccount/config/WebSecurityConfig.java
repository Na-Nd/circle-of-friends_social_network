package ru.nand.eurekaclientaccount.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.nand.eurekaclientaccount.util.JwtRequestFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    public WebSecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        System.out.println("===================SFC=====================");
        http.csrf().disable()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/profile/**").authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Не сохранять состояние сессий

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Добавлять jwtRequestFilter перед UsernamePasswordAuthenticationFilter в цепочку фильтров. Это необходимо для проверки JWT токенов в каждом запросе до того, как Spring Security проверит аутентификацию с помощью имени пользователя и пароля
        return http.build();
    }

}
