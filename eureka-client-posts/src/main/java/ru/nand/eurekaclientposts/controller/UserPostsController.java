package ru.nand.eurekaclientposts.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nand.eurekaclientposts.dto.PostDTO;
import ru.nand.eurekaclientposts.models.Post;
import ru.nand.eurekaclientposts.services.PostService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class UserPostsController {
    private final PostService postService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserPostsController(PostService postService, ModelMapper modelMapper) {
        this.postService = postService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/{username}/create")
    public String createNewPost(@PathVariable String username, @Valid @RequestBody PostDTO postDTO) {
        postDTO.setOwner(username);

        Post post = convertToPost(postDTO);
        postService.save(post);
        return "Post created successfully";
    }

    @GetMapping("/{username}")
    public List<PostDTO> getUserPosts(@PathVariable String username) {
        List<Post> posts = postService.findByOwner(username);
        return posts.stream()
                .map(this::convertToPostDTO)
                .collect(Collectors.toList());
    }

    private Post convertToPost(PostDTO postDTO) {
        return modelMapper.map(postDTO, Post.class);
    }

    private PostDTO convertToPostDTO(Post post) {
        return modelMapper.map(post, PostDTO.class);
    }


}
