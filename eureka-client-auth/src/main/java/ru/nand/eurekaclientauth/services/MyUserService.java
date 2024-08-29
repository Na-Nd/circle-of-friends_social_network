package ru.nand.eurekaclientauth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nand.eurekaclientauth.models.MyUser;
import ru.nand.eurekaclientauth.repositories.MyUserRepository;
import ru.nand.eurekaclientauth.util.MyUserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MyUserService {
    private final MyUserRepository myUserRepository;
    private final UserEventProducerService userEventProducerService;

    @Autowired
    public MyUserService(MyUserRepository myUserRepository, UserEventProducerService userEventProducerService) {
        this.myUserRepository = myUserRepository;
        this.userEventProducerService = userEventProducerService;
    }

    public List<MyUser> findAll(){
        return myUserRepository.findAll();
    }

    public MyUser findOne(int id){
        Optional<MyUser> foundUser = myUserRepository.findById(id);
        return foundUser.orElseThrow(MyUserNotFoundException::new);
    }

    public MyUser findByUsername(String username){
        return myUserRepository.findByUserName(username);
    }

    @Transactional
    public void save(MyUser user){ // todo
        enrichUser(user);
        myUserRepository.save(user);
    }

    private void enrichUser(MyUser user){
//        user.setCreatedAt(LocalDateTime.now());
//        user.setUpdatedAt(LocalDateTime.now());
//        user.setCreatedWho("auth microservice");
        Optional<MyUser> existingUser = myUserRepository.findById(user.getId());
        if(existingUser.isPresent()){
            user.setUpdatedAt(LocalDateTime.now());
        } else {
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user.setCreatedWho("auth microservice");
        }

    }
}
