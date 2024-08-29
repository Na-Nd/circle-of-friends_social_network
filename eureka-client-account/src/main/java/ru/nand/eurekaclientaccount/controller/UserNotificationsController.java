package ru.nand.eurekaclientaccount.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nand.eurekaclientaccount.dto.NotificationDTO;
import ru.nand.eurekaclientaccount.services.NotificationsService;
import ru.nand.eurekaclientaccount.util.JwtUtil;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class UserNotificationsController {

    private final NotificationsService notificationsService;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public UserNotificationsController(NotificationsService notificationsService, JwtUtil jwtUtil, ModelMapper modelMapper) {
        this.notificationsService = notificationsService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/notifications")
    private List<NotificationDTO> getUserNotifications(@AuthenticationPrincipal String username, @RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");
        String tokenUsername = jwtUtil.extractUsername(token);

        return notificationsService.getUserNotifications(tokenUsername, token);
    }
}
