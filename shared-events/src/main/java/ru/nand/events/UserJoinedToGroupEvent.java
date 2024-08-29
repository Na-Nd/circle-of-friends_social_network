package ru.nand.events;

public class UserJoinedToGroupEvent {
    private String username;
    private String group;

    public UserJoinedToGroupEvent(String username, String group) {
        this.username = username;
        this.group = group;
    }

    public UserJoinedToGroupEvent() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
