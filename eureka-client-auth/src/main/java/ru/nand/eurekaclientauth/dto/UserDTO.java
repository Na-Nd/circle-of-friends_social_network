package ru.nand.eurekaclientauth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserDTO { // Для микросервиса аккаунта
    @NotEmpty(message = "Username should be not empty!")
    @Size(min = 2, max = 30, message = "Username should be from 2 to 30 characters")
    private String username;

    @Email
    @NotEmpty(message = "Email should be not empty!")
    private String email;

    @NotEmpty(message = "Password should be not empty!")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
