package ru.nand.eurekaclientgroups.models.DTO;

public class GroupMessageShowDTO {
    private String content;
    private String sender;

    public GroupMessageShowDTO(String content, String sender) {
        this.content = content;
        this.sender = sender;
    }

    public GroupMessageShowDTO() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
