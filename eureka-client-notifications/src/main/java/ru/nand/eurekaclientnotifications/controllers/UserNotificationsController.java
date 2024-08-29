package ru.nand.eurekaclientnotifications.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nand.eurekaclientnotifications.dto.NotificationDTO;
import ru.nand.eurekaclientnotifications.models.Notification;
import ru.nand.eurekaclientnotifications.services.NotificationService;
import ru.nand.eurekaclientnotifications.util.JwtUtil;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notifications")
public class UserNotificationsController {
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    public UserNotificationsController(NotificationService notificationService, ModelMapper modelMapper, JwtUtil jwtUtil) {
        this.notificationService = notificationService;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/usernotifications")
    public List<NotificationDTO> getUserNotifications(@RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");
        String username  = jwtUtil.extractUsername(token);
        List<Notification> notifications = notificationService.findNotificationsByUsername(username);

        return notifications.stream()
                .map(this::convertToNotificationDTO)
                .collect(Collectors.toList());
    }

    private NotificationDTO convertToNotificationDTO(Notification notification){
        return modelMapper.map(notification, NotificationDTO.class);
    }
}
