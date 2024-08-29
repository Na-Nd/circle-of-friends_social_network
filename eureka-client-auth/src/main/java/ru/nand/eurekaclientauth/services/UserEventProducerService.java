package ru.nand.eurekaclientauth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.nand.events.UserRegistrationEvent;
import ru.nand.events.UserLoginEvent;

@Service
public class UserEventProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public UserEventProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUserRegistrationEvent(UserRegistrationEvent event) {
        kafkaTemplate.send("user-registration-topic", event);
    }

    public void sendUserLoginEvent(UserLoginEvent event) {
        kafkaTemplate.send("user-login-topic", event);
    }
}
