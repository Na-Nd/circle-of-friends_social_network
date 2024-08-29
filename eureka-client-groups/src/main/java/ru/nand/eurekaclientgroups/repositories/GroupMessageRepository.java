package ru.nand.eurekaclientgroups.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nand.eurekaclientgroups.models.Group;
import ru.nand.eurekaclientgroups.models.GroupMessage;

import java.util.List;

@Repository
public interface GroupMessageRepository extends JpaRepository<GroupMessage, Integer> {

    List<GroupMessage> findByGroup(Group group);

}
