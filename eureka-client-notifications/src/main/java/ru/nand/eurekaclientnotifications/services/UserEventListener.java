package ru.nand.eurekaclientnotifications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.nand.events.UserJoinedToGroupEvent;
import ru.nand.events.UserMessagesEvent;
import ru.nand.events.UserRegistrationEvent;
import ru.nand.events.UserLoginEvent;
import ru.nand.eurekaclientnotifications.models.Notification;


@Service
public class UserEventListener {
    private final NotificationService notificationService;


    @Autowired
    public UserEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "user-registration-topic", groupId = "notification-group")
    public void handleUserRegistration(UserRegistrationEvent event) {
        Notification notification = new Notification(event.getUsername(), "User registered");
        notificationService.save(notification);
        System.out.println("Received User Registration Event: " + event.getUsername());
    }

    @KafkaListener(topics = "user-login-topic", groupId = "notification-group")
    public void handleUserLogin(UserLoginEvent event) {
        Notification notification = new Notification(event.getUsername(), "User logged in");
        notificationService.save(notification);
        System.out.println("Received User Login Event: " + event.getUsername());
    }

    @KafkaListener(topics = "user-messages-topic", groupId = "notification-group")
    public void handleUserMessages(UserMessagesEvent event){
        Notification notification = new Notification(event.getRecipient(), "You have a message from the user: " + event.getSender());
        notificationService.save(notification);
        System.out.println("Received User Messages Event: " + event.getRecipient());
    }

    @KafkaListener(topics = "user_group_notifications", groupId = "notification-group")
    public void handleUserGroup(UserJoinedToGroupEvent event){
        Notification notification = new Notification(event.getUsername(), "User with name " + event.getUsername() + " has joined the group " + event.getGroup());
        notificationService.save(notification);
        System.out.println("Received event: " + event.getUsername());
    }
}
