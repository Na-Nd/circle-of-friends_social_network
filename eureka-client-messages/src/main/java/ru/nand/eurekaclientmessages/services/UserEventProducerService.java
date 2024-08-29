package ru.nand.eurekaclientmessages.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.nand.events.UserMessagesEvent;

@Service
public class UserEventProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public UserEventProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserMessagesEvent(UserMessagesEvent userMessagesEvent){
        kafkaTemplate.send("user-messages-topic", userMessagesEvent);
    }
}
