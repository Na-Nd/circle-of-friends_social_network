package ru.nand.eurekaclientgroups.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.nand.events.UserGroupMessagesEvent;

@Service
public class GroupMessagesProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public GroupMessagesProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendGroupMessageEvent(UserGroupMessagesEvent userGroupMessagesEvent){
        kafkaTemplate.send("group_messages_notifications", userGroupMessagesEvent);
    }
}
