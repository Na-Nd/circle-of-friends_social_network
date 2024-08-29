package ru.nand.events;

public class UserRegistrationEvent {
    private String username;
    private String email;

    public UserRegistrationEvent() {
    }

    public UserRegistrationEvent(String username, String email) {
        this.username = username;
        this.email = email;
    }

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
}
