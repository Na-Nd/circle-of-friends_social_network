package ru.nand.eurekaclientgroups.models.DTO;

public class GroupMessageDTO {
    private String content;

    public GroupMessageDTO(String content) {
        this.content = content;
    }

    public GroupMessageDTO() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
