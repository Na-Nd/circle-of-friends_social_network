package ru.nand.eurekaclientaccount.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.nand.eurekaclientaccount.dto.PostDTO;
import ru.nand.eurekaclientaccount.services.PostService;
import ru.nand.eurekaclientaccount.util.JwtUtil;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class UserPostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserPostController(PostService postService, JwtUtil jwtUtil) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/{username}/createNewPost")
    public String createNewPost(@PathVariable String username, @Valid @RequestBody PostDTO postDTO, @RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "").trim();
        String tokenUsername = jwtUtil.extractUsername(token);

        if (!tokenUsername.equals(username)) {
            throw new SecurityException("You do not have permission to create a post for this profile.");
        }

        postDTO.setOwner(username);
        return postService.createPost(username, postDTO, token);
    }

    @GetMapping("/{username}/posts")
    public List<PostDTO> getUserPosts(@PathVariable String username, @RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "").trim();
        String tokenUsername = jwtUtil.extractUsername(token);

        return postService.getUserPosts(username, token);
    }

}
