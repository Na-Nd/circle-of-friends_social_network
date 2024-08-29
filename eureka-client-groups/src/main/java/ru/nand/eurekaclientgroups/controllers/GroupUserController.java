package ru.nand.eurekaclientgroups.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.nand.eurekaclientgroups.services.GroupService;
import ru.nand.eurekaclientgroups.models.Group;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupUserController {
    private final GroupService groupService;

    @Autowired
    public GroupUserController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/{groupName}/users")
    public List<String> getUsersByGroupName(@PathVariable String groupName, @RequestHeader("Authorization") String authorizationHeader) {
        return groupService.findGroupByName(groupName)
                .map(group -> new ArrayList<>(group.getUsers()))
                .orElseThrow(() -> new RuntimeException("Group not found"));
    }

}
