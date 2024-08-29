package ru.nand.eurekaclientnotifications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.nand.eurekaclientnotifications.models.Notification;
import ru.nand.eurekaclientnotifications.repository.NotificationRepository;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final GroupServiceClient groupServiceClient;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, GroupServiceClient groupServiceClient) {
        this.notificationRepository = notificationRepository;
        this.groupServiceClient = groupServiceClient;
    }
    void save(Notification notification){
        notificationRepository.save(notification);
    }
    @Cacheable(value = "notifications", key = "#username") // Если в кэше есть данные, то вернет из кэша. Иначе они загрузятся из бд и закэшируются
    public List<Notification> findNotificationsByUsername(String username){
        return notificationRepository.findNotificationsByUsername(username);
    }
    public List<String> getGroupMembers(String groupName, String jwtToken) {
        return groupServiceClient.getGroupMembers(groupName, jwtToken);
    }

}
