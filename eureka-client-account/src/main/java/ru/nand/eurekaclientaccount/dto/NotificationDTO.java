package ru.nand.eurekaclientaccount.dto;

import jakarta.validation.constraints.NotEmpty;

public class NotificationDTO {
    private String username;
    @NotEmpty(message = "Message should be not empty")
    private String message;

    public NotificationDTO() {
    }

    public NotificationDTO(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
