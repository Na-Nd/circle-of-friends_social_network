package ru.nand.eurekaclientmessages.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.nand.eurekaclientmessages.dto.MessageDTO;
import ru.nand.eurekaclientmessages.models.Message;
import ru.nand.eurekaclientmessages.services.MessageService;
import ru.nand.eurekaclientmessages.services.UserEventProducerService;
import ru.nand.eurekaclientmessages.util.JwtUtil;
import ru.nand.events.UserMessagesEvent;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    private final UserEventProducerService userEventProducerService;

    @Autowired
    public MessageController(MessageService messageService, ModelMapper modelMapper, JwtUtil jwtUtil, UserEventProducerService userEventProducerService) {
        this.messageService = messageService;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
        this.userEventProducerService = userEventProducerService;
    }

    @GetMapping("/messages")
    public List<Message> getUserMessages(@AuthenticationPrincipal String username){
        return messageService.findMessagesByRecipient(username);
    }

    @PostMapping("/create")
    public String createMessage(@Valid @RequestBody MessageDTO messageDTO, @RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "").trim();
        String sender = jwtUtil.extractUsername(token);

        Message message = convertToMessage(messageDTO);
        message.setSender(sender);
        message.setTimestamp(LocalDateTime.now());

        messageService.save(message);
        System.out.println("Получилось сообщение:" + message.toString());

        UserMessagesEvent userMessagesEvent = new UserMessagesEvent();
        userMessagesEvent.setSender(sender);
        userMessagesEvent.setRecipient(message.getRecipient());
        userEventProducerService.sendUserMessagesEvent(userMessagesEvent);

        return "Message created successfully";
    }


    private Message convertToMessage(MessageDTO messageDTO) { // Устанавливается recipient и content
        return modelMapper.map(messageDTO, Message.class);
    }

    private MessageDTO convertToMessageDTO(Message message) {
        return modelMapper.map(message, MessageDTO.class);
    }


}
