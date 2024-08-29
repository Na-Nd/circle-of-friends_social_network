package ru.nand.eurekaclientauth.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.nand.eurekaclientauth.dto.MyUserDTO;
import ru.nand.events.UserRegistrationEvent;
import ru.nand.events.UserLoginEvent;
import ru.nand.eurekaclientauth.models.MyUser;
import ru.nand.eurekaclientauth.services.MyUserDetailsServiceImpl;
import ru.nand.eurekaclientauth.services.MyUserService;
import ru.nand.eurekaclientauth.services.UserEventProducerService;
import ru.nand.eurekaclientauth.util.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final MyUserDetailsServiceImpl myUserDetailsService;
    private final MyUserService myUserService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    private final UserEventProducerService userEventProducerService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, MyUserDetailsServiceImpl myUserDetailsService, MyUserService myUserService, ModelMapper modelMapper, PasswordEncoder passwordEncoder, UserEventProducerService userEventProducerService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.myUserDetailsService = myUserDetailsService;
        this.myUserService = myUserService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userEventProducerService = userEventProducerService;
    }

    @GetMapping("/test")
    public String test(){
        return "test complete successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            System.out.println("Контроллер, передан такой пользователь: " + authenticationRequest.getUserName() + ", пароль:" + authenticationRequest.getPassword());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password: " + e.getMessage());
        }

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUserName());
        final String jwt = jwtUtil.generateToken(userDetails);

        // Отправляем событие логина пользователя в Kafka
        UserLoginEvent event = new UserLoginEvent();
        event.setUsername(authenticationRequest.getUserName());
        userEventProducerService.sendUserLoginEvent(event);

        System.out.println("Сгенерировал JWT при логине:" + jwt);
        //kafkaProducer.sendMessage(authenticationRequest.getUserName(), "User logged in: " + authenticationRequest.getUserName());
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid MyUserDTO myUserDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError>errors =bindingResult.getFieldErrors();
            for(FieldError error : errors){
                errorMsg.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";");
            }

            throw new MyUserNotCreatedException(errorMsg.toString());
        }

        myUserDTO.setPassword(passwordEncoder.encode(myUserDTO.getPassword()));
        MyUser myUser = convertToMyUser(myUserDTO);
        myUserService.save(myUser);

        // Отправляем событие регистрации пользователя в Kafka
        UserRegistrationEvent event = new UserRegistrationEvent();
        event.setUsername(myUser.getUserName());
        event.setEmail(myUser.getEmail());
        userEventProducerService.sendUserRegistrationEvent(event);

        String jwt =jwtUtil.generateToken(myUserDetailsService.loadUserByUsername(myUser.getUserName()));
        System.out.println("Сгенерировал JWT при регистрации:" + jwt);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    public MyUser convertToMyUser(MyUserDTO myUserDTO){
        return modelMapper.map(myUserDTO, MyUser.class);
    }


    @ExceptionHandler
    public ResponseEntity<MyUserErrorResponse> handleException(MyUserNotFoundException e){
        MyUserErrorResponse response = new MyUserErrorResponse("User with this id was not found! ", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<MyUserErrorResponse> handleException(MyUserNotCreatedException e){
        MyUserErrorResponse response = new MyUserErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
