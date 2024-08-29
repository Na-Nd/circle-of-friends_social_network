package ru.nand.eurekaclientnotifications.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nand.eurekaclientnotifications.models.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUsername(String username);

    List<Notification> findNotificationsByUsername(String username);
}
