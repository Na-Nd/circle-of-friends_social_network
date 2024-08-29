package ru.nand.eurekaclientauth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nand.eurekaclientauth.models.MyUser;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Integer> {
    MyUser findByEmail(String email);

    MyUser findByUserName(String username);
}
