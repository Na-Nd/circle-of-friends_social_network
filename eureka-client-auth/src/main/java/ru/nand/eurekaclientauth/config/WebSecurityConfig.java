package ru.nand.eurekaclientauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.nand.eurekaclientauth.services.MyUserDetailsServiceImpl;
import ru.nand.eurekaclientauth.util.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final MyUserDetailsServiceImpl myUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;


    @Autowired
    public WebSecurityConfig(PasswordEncoder passwordEncoder, MyUserDetailsServiceImpl myUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.passwordEncoder = passwordEncoder;
        this.myUserDetailsService = myUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        System.out.println("===================SFC=====================");
        http.csrf().disable()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/register").permitAll()
                        .requestMatchers("/auth/test").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Не сохранять состояние сессий

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Добавлять jwtRequestFilter перед UsernamePasswordAuthenticationFilter в цепочку фильтров. Это необходимо для проверки JWT токенов в каждом запросе до того, как Spring Security проверит аутентификацию с помощью имени пользователя и пароля
        return http.build();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{ // Получить менеджер из конфигурации, чтобы использовать этот менеджер для аутентификации пользователей
        return configuration.getAuthenticationManager();
    }
}
