package ru.nand.eurekaclientauth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.nand.eurekaclientauth.models.MyUser;
import ru.nand.eurekaclientauth.repositories.MyUserRepository;

import java.util.ArrayList;

@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    private final MyUserRepository repository;

    @Autowired
    public MyUserDetailsServiceImpl(MyUserRepository repository) {
        this.repository = repository;
    }

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        System.out.println("!!!!!!!!!!!В MyUserDetailsServiceImpl, метод UserDetails loadUserByUsername()!!!!!!!!!!!");
//        System.out.println("Передан: " + email);
//
//        MyUser user = repository.findByEmail(email); // Используем почту для поиска пользователя
//        if (user == null) {
//            throw new UsernameNotFoundException("User with email: " + email + " not found");
//        }
//
//        System.out.println("Параметры пользователя: " + user.getUserName() + ", пароль: " + user.getPassword());
//
//        return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("!!!!!!!!!!!В MyUserDetailsServiceImpl, метод UserDetails loadUserByUsername()!!!!!!!!!!!");
        System.out.println("Передан: " + username);

        MyUser user = repository.findByUserName(username); // Используем почту для поиска пользователя
        if (user == null) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }

        System.out.println("Параметры пользователя: " + user.getUserName() + ", пароль: " + user.getPassword());

        return new User(user.getUserName(), user.getPassword(), new ArrayList<>());
    }

}
