package ru.nand.eurekaclientmessages.dto;

public class MessageDTO {
    private String recipient;
    private String content;

    public MessageDTO() {
    }

    public MessageDTO(String recipient, String content) {
        this.recipient = recipient;
        this.content = content;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
