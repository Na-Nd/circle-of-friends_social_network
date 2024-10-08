package ru.nand.eurekaclientnotifications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.nand.events.*;
import ru.nand.eurekaclientnotifications.models.Notification;

import java.util.List;


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
    public void handleUserGroup(UserJoinedToGroupEvent event) {
        List<String> usersInGroup = notificationService.getUsersByGroupName(event.getGroup());

        for (String user : usersInGroup) {
            Notification notification = new Notification(user, "User " + event.getUsername() + " has joined the group " + event.getGroup());
            notificationService.save(notification);
        }

        System.out.println("Received UserJoinedToGroupEvent for group: " + event.getGroup());
    }

    @KafkaListener(topics = "group_messages_notifications", groupId = "notification-group")
    public void handleGroupMessageEvent(UserGroupMessagesEvent event) {
        String groupName = event.getGroup();
        String username = event.getSender();

        // Получение всех пользователей группы
        List<String> groupUsers = notificationService.getUsersByGroupName(groupName);

        // Создание уведомления для каждого пользователя
        for (String user : groupUsers) {
            if (!user.equals(username)) {  // исключаем отправителя из списка получателей
                Notification notification = new Notification(user, "User " + username + " sent a message in group " + groupName);
                notificationService.save(notification);
            }
        }
        System.out.println("Received group message event for group: " + groupName + " from user: " + username);
    }




}
