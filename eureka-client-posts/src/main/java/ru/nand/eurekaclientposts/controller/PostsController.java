package ru.nand.eurekaclientposts.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.nand.eurekaclientposts.dto.PostDTO;
import ru.nand.eurekaclientposts.models.Post;
import ru.nand.eurekaclientposts.services.PostService;
import ru.nand.eurekaclientposts.util.JwtUtil;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostsController {
    private final PostService postService;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    @Autowired
    public PostsController(PostService postService, ModelMapper modelMapper, JwtUtil jwtUtil) {
        this.postService = postService;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/create")
    public String createPost(@Valid @RequestBody PostDTO postDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "").trim();
        String username = jwtUtil.extractUsername(token);

        postDTO.setOwner(username); // Устанавливаем имя пользователя из токена
        Post post = convertToPost(postDTO);
        postService.save(post);
        return "Post created successfully";
    }

    @GetMapping("/all")
    public List<PostDTO> showAllPosts() {
        return postService.findAll().stream()
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
