package ru.nand.eurekaclientgroups.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.nand.eurekaclientgroups.models.DTO.GroupDTO;
import ru.nand.eurekaclientgroups.models.Group;
import ru.nand.eurekaclientgroups.services.GroupMessagesProducerService;
import ru.nand.eurekaclientgroups.services.GroupNotificationsProducerService;
import ru.nand.eurekaclientgroups.services.GroupService;
import ru.nand.eurekaclientgroups.util.JwtUtil;
import ru.nand.events.UserGroupMessagesEvent;
import ru.nand.events.UserJoinedToGroupEvent;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final GroupNotificationsProducerService groupNotificationsProducerService;
    private final GroupMessagesProducerService groupMessagesProducerService;

    @Autowired
    public GroupController(GroupService groupService, JwtUtil jwtUtil, ModelMapper modelMapper, GroupNotificationsProducerService groupNotificationsProducerService, GroupMessagesProducerService groupMessagesProducerService) {
        this.groupService = groupService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.groupNotificationsProducerService = groupNotificationsProducerService;
        this.groupMessagesProducerService = groupMessagesProducerService;
    }

    @PostMapping("/create")
    public String createGroup(@Valid @RequestBody GroupDTO groupDTO, @RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "").trim();
        String creator = jwtUtil.extractUsername(token);

        Group group = convertToGroup(groupDTO); // Устанавливается название группы
        group.setCreator(creator);
        groupService.createGroup(group.getName(), group.getCreator());

        UserJoinedToGroupEvent userJoinedToGroupEvent = new UserJoinedToGroupEvent();
        userJoinedToGroupEvent.setUsername(creator);
        userJoinedToGroupEvent.setGroup(group.getName());

        groupNotificationsProducerService.sendGroupNotificationEvent(userJoinedToGroupEvent);

        return "Group " + group.getName() + " created successfully";
    }

    @GetMapping("/all")
    public List<GroupDTO> getAllGroups(){
        return groupService.gelAllGroups().stream()
                .map(this::convertToGroupDTO).collect(Collectors.toList());
    }

    @PostMapping("/{groupName}/join")
    public String joinGroup(@PathVariable String groupName, @RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "").trim();
        String username = jwtUtil.extractUsername(token);

        groupService.joinGroup(groupName, username);

        UserJoinedToGroupEvent userJoinedToGroupEvent = new UserJoinedToGroupEvent();
        userJoinedToGroupEvent.setUsername(username);
        userJoinedToGroupEvent.setGroup(groupName);

        groupNotificationsProducerService.sendGroupNotificationEvent(userJoinedToGroupEvent);

        return "User " + username + " joined group " + groupName;
    }

    private Group convertToGroup(GroupDTO groupDTO){
        return modelMapper.map(groupDTO, Group.class);
    }

    private GroupDTO convertToGroupDTO(Group group){
        return modelMapper.map(group, GroupDTO.class);
    }
}
