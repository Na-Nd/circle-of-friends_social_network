package ru.nand.eurekaclientaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EurekaClientAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientAccountApplication.class, args);
    }

}
