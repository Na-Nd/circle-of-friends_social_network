package ru.nand.events;

public class UserGroupMessagesEvent {
    private String sender;
    private String group;

    public UserGroupMessagesEvent(String sender, String group) {
        this.sender = sender;
        this.group = group;
    }

    public UserGroupMessagesEvent() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
