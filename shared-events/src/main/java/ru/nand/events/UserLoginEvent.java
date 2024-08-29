package ru.nand.events;

public class UserLoginEvent {
    private String username;

    public UserLoginEvent(String username) {
        this.username = username;
    }

    public UserLoginEvent() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
