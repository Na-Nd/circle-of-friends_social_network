package ru.nand.eurekaclientgroups.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nand.eurekaclientgroups.models.Group;
import ru.nand.eurekaclientgroups.models.GroupMessage;
import ru.nand.eurekaclientgroups.repositories.GroupMessageRepository;
import ru.nand.eurekaclientgroups.repositories.GroupRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupMessageService {

    private final GroupMessageRepository groupMessageRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public GroupMessageService(GroupMessageRepository groupMessageRepository, GroupRepository groupRepository) {
        this.groupMessageRepository = groupMessageRepository;
        this.groupRepository = groupRepository;
    }

    public GroupMessage addMessageToGroup(String groupName, String sender, String content){
        Group group = groupRepository.findByName(groupName).orElseThrow(() -> new IllegalArgumentException("Group with name: " + groupName + " nut found :("));

        GroupMessage message = new GroupMessage(sender, content, LocalDateTime.now(), group);
        return groupMessageRepository.save(message);
    }

    public List<GroupMessage> getMessagesForGroup(String groupName){
        Group group = groupRepository.findByName(groupName).orElseThrow(() -> new IllegalArgumentException("Group with name: " + groupName + " nut found :("));

        return groupMessageRepository.findByGroup(group);
    }
}
