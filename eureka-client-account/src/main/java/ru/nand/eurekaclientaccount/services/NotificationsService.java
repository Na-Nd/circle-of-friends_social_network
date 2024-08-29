package ru.nand.eurekaclientaccount.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.nand.eurekaclientaccount.dto.NotificationDTO;

import java.util.Arrays;
import java.util.List;

@Service
public class NotificationsService {

    @Value("${notifications.service.url}")
    private String notificationsServiceUrl;

    private final RestTemplate restTemplate;


    @Autowired
    public NotificationsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<NotificationDTO> getUserNotifications(String username, String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        HttpEntity<NotificationDTO[]> response = restTemplate.exchange(
                notificationsServiceUrl + "/notifications/usernotifications",
                HttpMethod.GET,
                entity,
                NotificationDTO[].class
        );

        return Arrays.asList(response.getBody());
    }
}
