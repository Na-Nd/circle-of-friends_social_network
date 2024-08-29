package ru.nand.eurekaclientmessages.services;

import org.springframework.stereotype.Service;
import ru.nand.eurekaclientmessages.models.Message;
import ru.nand.eurekaclientmessages.repositories.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> findMessagesByRecipient(String username){
        return messageRepository.findMessagesByRecipient(username);
    }

    public void enrichMessage(Message message){
        message.setTimestamp(LocalDateTime.now());
    }

    public void save(Message message){
        messageRepository.save(message);
    }
}
