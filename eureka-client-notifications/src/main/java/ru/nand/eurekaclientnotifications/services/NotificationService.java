package ru.nand.eurekaclientnotifications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.nand.eurekaclientnotifications.models.Notification;
import ru.nand.eurekaclientnotifications.repository.NotificationRepository;
import ru.nand.eurekaclientnotifications.util.JwtUtil;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    @Value("${groups.service.url}") // http://localhost:8082
    private String groupsServiceUrl;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, RestTemplate restTemplate, JwtUtil jwtUtil) {
        this.notificationRepository = notificationRepository;
        this.restTemplate = restTemplate;
        this.jwtUtil = jwtUtil;
    }

    void save(Notification notification){
        notificationRepository.save(notification);
    }
    @Cacheable(value = "notifications", key = "#username") // Если в кэше есть данные, то вернет из кэша. Иначе они загрузятся из бд и закэшируются
    public List<Notification> findNotificationsByUsername(String username){
        return notificationRepository.findNotificationsByUsername(username);
    }

    public List<String> getUsersByGroupName(String groupName) {
        String url = groupsServiceUrl + "/groups/" + groupName + "/users";  // URL до микросервиса групп
        String token = jwtUtil.generateToken();  // Генерация JWT токена

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<String>> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<String>>() {});

        return response.getBody();
    }


}
