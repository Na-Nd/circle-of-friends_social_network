package ru.nand.eurekaclientgroups.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.nand.eurekaclientgroups.models.DTO.GroupMessageDTO;
import ru.nand.eurekaclientgroups.models.DTO.GroupMessageShowDTO;
import ru.nand.eurekaclientgroups.models.GroupMessage;
import ru.nand.eurekaclientgroups.services.GroupMessageService;
import ru.nand.eurekaclientgroups.services.GroupMessagesProducerService;
import ru.nand.eurekaclientgroups.services.GroupService;
import ru.nand.eurekaclientgroups.util.JwtUtil;
import ru.nand.events.UserGroupMessagesEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/groups/{groupName}/messages")
public class GroupMessageController {
    private final GroupMessageService groupMessageService;
    private final GroupService groupService;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final GroupMessagesProducerService groupMessagesProducerService;

    @Autowired
    public GroupMessageController(GroupMessageService groupMessageService, GroupService groupService, JwtUtil jwtUtil, ModelMapper modelMapper, GroupMessagesProducerService groupMessagesProducerService) {
        this.groupMessageService = groupMessageService;
        this.groupService = groupService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.groupMessagesProducerService = groupMessagesProducerService;
    }

    @PostMapping
    public String addMessageToGroup(@PathVariable String groupName, @RequestBody GroupMessageDTO groupMessageDTO, @RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "").trim();
        String username = jwtUtil.extractUsername(token);

        groupService.findGroupByName(groupName).filter(group -> group.getUsers().contains(username))
                .orElseThrow(() -> new RuntimeException("Access denied: You are not a member of this group"));

        GroupMessage groupMessage = convertToGroupMessage(groupMessageDTO); // Тут только контент
        groupMessage.setSender(username);
        groupMessage.setTimestamp(LocalDateTime.now());

        groupMessageService.addMessageToGroup(groupName, groupMessage.getSender(), groupMessage.getContent());

        UserGroupMessagesEvent userGroupMessagesEvent = new UserGroupMessagesEvent();
        userGroupMessagesEvent.setSender(username);
        userGroupMessagesEvent.setGroup(groupName);
        //kafkaProducerService.sendMessage("group_notifications_" + groupName, "User " + username + " sent a message to group " + groupName);
        groupMessagesProducerService.sendGroupMessageEvent(userGroupMessagesEvent);

        return "Message to group: " + groupName + " created successfully";
    }

    @GetMapping
    public List<GroupMessageShowDTO> getGroupMessages(@PathVariable String groupName, @RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "").trim();
        String username = jwtUtil.extractUsername(token);

        groupService.findGroupByName(groupName).filter(group -> group.getUsers().contains(username))
                .orElseThrow(() -> new RuntimeException("Access denied: You are not a member of this group"));

        return groupMessageService.getMessagesForGroup(groupName).stream().map(this::convertToGroupMessageShowDTO).collect(Collectors.toList());
    }

    private GroupMessage convertToGroupMessage(GroupMessageDTO dto){
        return modelMapper.map(dto, GroupMessage.class);
    }

    private GroupMessageDTO convertToGroupMessageDTO(GroupMessage message){
        return modelMapper.map(message, GroupMessageDTO.class);
    }

    private GroupMessageShowDTO convertToGroupMessageShowDTO(GroupMessage message){
        return modelMapper.map(message, GroupMessageShowDTO.class);
    }
}
