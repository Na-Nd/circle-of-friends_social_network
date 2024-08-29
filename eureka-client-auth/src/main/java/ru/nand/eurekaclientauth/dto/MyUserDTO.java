package ru.nand.eurekaclientauth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class MyUserDTO {
    @NotEmpty(message = "Username should be empty!")
    @Size(min = 2, max = 30, message = "Username should be from 2 to 30 characters")
    private String userName;


    @Email
    @NotEmpty(message = "Email should be not empty!")
    private String email;


    @NotEmpty(message = "Password should be not empty!")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
