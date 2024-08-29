package ru.nand.eurekaclientaccount.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.nand.eurekaclientaccount.dto.UserDTO;
import ru.nand.eurekaclientaccount.services.UserService;
import ru.nand.eurekaclientaccount.util.JwtUtil;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    @Value("${auth.service.url}")
    private String authServiceUrl;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserProfileController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{username}")
    public UserDTO getUser(
            @PathVariable String username,
            @RequestHeader("Authorization") String authorizationHeader) {

        // Извлекаем токен из заголовка
        String token = authorizationHeader.replace("Bearer ", "").trim();

        String tokenUsername = jwtUtil.extractUsername(token);

        UserDTO userDTO = userService.getUser(username, token);
        if(!tokenUsername.equals(username)){
            userDTO.setPassword(null);
        }

        // На фронте можно будет что-то делать, если пароль не null
        return userDTO;
    }

    @PutMapping("/{username}")
    public String updateUser(
            @PathVariable String username,
            @Valid @RequestBody UserDTO userDTO,
            @RequestHeader("Authorization") String authorizationHeader) {

        // Извлекаем токен из заголовка
        String token = authorizationHeader.replace("Bearer ", "").trim();

        String tokenUsername = jwtUtil.extractUsername(token);

        // Проверяем, что запрашиваемое имя пользователя совпадает с именем пользователя в токене
        if (!tokenUsername.equals(username)) {
            throw new SecurityException("You do not have permission to edit this profile.");
        }

        return userService.updateUser(username, userDTO, token);
    }
}
