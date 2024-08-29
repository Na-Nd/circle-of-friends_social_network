package ru.nand.eurekaclientaccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.nand.eurekaclientaccount.dto.MessageDTO;
import ru.nand.eurekaclientaccount.services.MessagesService;
import ru.nand.eurekaclientaccount.util.JwtUtil;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class UserMessageController {

    private final JwtUtil jwtUtil;
    private final MessagesService messagesService;

    @Autowired
    public UserMessageController(JwtUtil jwtUtil, MessagesService messagesService) {
        this.jwtUtil = jwtUtil;
        this.messagesService = messagesService;
    }

    @GetMapping("/messages/get")
    public List<MessageDTO> getUserMessages(@RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer", "");
        String username = jwtUtil.extractUsername(token);

        return messagesService.getUserMessages(token);
    }

    @PostMapping("/messages/create")
    public String createUserMessage(@RequestBody MessageDTO messageDTO, @RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "").trim();
        String username = jwtUtil.extractUsername(token);
        System.out.println("Передан: " + username);

        return messagesService.createUserMessage(messageDTO, token);
    }
}
