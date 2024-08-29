package ru.nand.eurekaclientposts.dto;

import jakarta.validation.constraints.NotEmpty;

public class PostDTO {
    private String owner;

    @NotEmpty(message = "Text should be not empty")
    private String postText;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }
}
