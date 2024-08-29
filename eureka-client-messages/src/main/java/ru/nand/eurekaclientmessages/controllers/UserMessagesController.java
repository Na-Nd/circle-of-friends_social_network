package ru.nand.eurekaclientmessages.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.nand.eurekaclientmessages.dto.MessageDTO;
import ru.nand.eurekaclientmessages.models.Message;
import ru.nand.eurekaclientmessages.services.MessageService;
import ru.nand.eurekaclientmessages.services.UserEventProducerService;
import ru.nand.eurekaclientmessages.util.JwtUtil;
import ru.nand.events.UserMessagesEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class UserMessagesController {

    private final JwtUtil jwtUtil;

    private final MessageService messageService;

    private final ModelMapper modelMapper;

    private final UserEventProducerService userEventProducerService;

    @Autowired
    public UserMessagesController(JwtUtil jwtUtil, MessageService messageService, ModelMapper modelMapper, UserEventProducerService userEventProducerService) {
        this.jwtUtil = jwtUtil;
        this.messageService = messageService;
        this.modelMapper = modelMapper;
        this.userEventProducerService = userEventProducerService;
    }

    @PostMapping("/create")
    public String createNewMessage(@RequestHeader("Authorization") String authorizationHeader, @RequestBody MessageDTO messageDTO){
        String token = authorizationHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);

        Message message = convertToMessage(messageDTO);
        message.setSender(username);
        message.setTimestamp(LocalDateTime.now());

        messageService.save(message);
        System.out.println("Получилось сообщение:" + message.toString());

        UserMessagesEvent userMessagesEvent = new UserMessagesEvent();
        userMessagesEvent.setSender(message.getSender());
        userMessagesEvent.setRecipient(message.getRecipient());
        userEventProducerService.sendUserMessagesEvent(userMessagesEvent);

        return "Message created successfully";
    }

    @GetMapping("/usermessages")
    public List<MessageDTO> getUserMessages(@RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);

        List<Message> messages = messageService.findMessagesByRecipient(username);
        return messages.stream()
                .map(this::convertToMessageDTO)
                .collect(Collectors.toList());
    }

    private Message convertToMessage(MessageDTO messageDTO) { // Устанавливается recipient и content
        return modelMapper.map(messageDTO, Message.class);
    }

    private MessageDTO convertToMessageDTO(Message message) {
        return modelMapper.map(message, MessageDTO.class);
    }

}
