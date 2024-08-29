package ru.nand.eurekaclientposts.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Owner should be not empty")
    @Column(name = "owner")
    private String owner;

    @NotEmpty(message = "Text should be not empty")
    @Column(name = "post_text")
    private String postText;

    @Column(name = "date_of_publication")
    private LocalDateTime dateOfPublication;

    public Post() {
    }

    public Post(String owner, String postText) {
        this.owner = owner;
        this.postText = postText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public LocalDateTime getDateOfPublication() {
        return dateOfPublication;
    }

    public void setDateOfPublication(LocalDateTime dateOfPublication) {
        this.dateOfPublication = dateOfPublication;
    }
}
