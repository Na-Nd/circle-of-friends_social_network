package ru.nand.eurekaclientgroups.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.nand.events.UserJoinedToGroupEvent;

@Service
public class GroupNotificationsProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public GroupNotificationsProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendGroupNotificationEvent(UserJoinedToGroupEvent event){
        kafkaTemplate.send("user_group_notifications", event);
    }

}
