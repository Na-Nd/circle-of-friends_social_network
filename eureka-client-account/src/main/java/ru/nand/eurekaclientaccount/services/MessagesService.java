package ru.nand.eurekaclientaccount.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.nand.eurekaclientaccount.dto.MessageDTO;
import ru.nand.eurekaclientaccount.util.JwtUtil;

import java.util.Arrays;
import java.util.List;

@Service
public class MessagesService {

    @Value("${messages.service.url}")
    private String messagesServiceUrl;

    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    public MessagesService(RestTemplate restTemplate, JwtUtil jwtUtil) {
        this.restTemplate = restTemplate;
        this.jwtUtil = jwtUtil;
    }

    public List<MessageDTO> getUserMessages(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        HttpEntity<MessageDTO[]> response = restTemplate.exchange(
                messagesServiceUrl + "/messages/usermessages",
                HttpMethod.GET,
                entity,
                MessageDTO[].class
        );

        return Arrays.asList(response.getBody());
    }

    public String createUserMessage(MessageDTO messageDTO, String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<MessageDTO> entity = new HttpEntity<>(messageDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                messagesServiceUrl + "/messages/create",
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody();
    }
}
