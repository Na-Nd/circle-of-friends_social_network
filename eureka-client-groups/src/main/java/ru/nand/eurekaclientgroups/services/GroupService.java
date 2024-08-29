package ru.nand.eurekaclientgroups.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.nand.eurekaclientgroups.models.Group;
import ru.nand.eurekaclientgroups.repositories.GroupRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private final GroupRepository groupRepository;


    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void createGroup(String groupName, String creator){
        Group group = new Group(groupName, creator);
        groupRepository.save(group);
    }

    public List<Group> gelAllGroups(){
        return groupRepository.findAll();
    }

    public Optional<Group> findGroupByName(String name){
        return groupRepository.findByName(name);
    }

    public void joinGroup(String groupName, String username){
        Group group = groupRepository.findByName(groupName).orElseThrow(() -> new IllegalArgumentException("Group with name: " + groupName + " nut found :("));
        group.getUsers().add(username);
        groupRepository.save(group); // todo
    }


}
