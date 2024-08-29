package ru.nand.events;

public class UserMessagesEvent {
    private String sender;
    private String recipient;

    public UserMessagesEvent(String sender, String recipient) {
        this.sender = sender;
        this.recipient = recipient;
    }

    public UserMessagesEvent() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
