package ru.nand.eurekaclientaccount.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.nand.eurekaclientaccount.dto.UserDTO;

@Service
public class UserService {

    @Value("${auth.service.url}")
    private String authServiceUrl;

    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(RestTemplate restTemplate, PasswordEncoder passwordEncoder) {
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO getUser(String username, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        System.out.println("User Service передал токен:" + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<UserDTO> response = restTemplate.exchange(
                authServiceUrl + "/users/" + username,
                HttpMethod.GET,
                entity,
                UserDTO.class
        );

        return response.getBody();
    }

    public String updateUser(String username, UserDTO userDTO, String token) {
        // Шифровать пароль перед отправкой запроса
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<UserDTO> entity = new HttpEntity<>(userDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                authServiceUrl + "/users/" + username,
                HttpMethod.PUT,
                entity,
                String.class
        );

        return response.getBody();
    }
}
