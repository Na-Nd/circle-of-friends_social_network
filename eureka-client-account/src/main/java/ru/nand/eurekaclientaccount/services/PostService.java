package ru.nand.eurekaclientaccount.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.nand.eurekaclientaccount.dto.PostDTO;

@Service
public class PostService {

    @Value("${post.service.url}")
    private String postServiceUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public PostService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String createPost(String username, PostDTO postDTO, String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<PostDTO> entity = new HttpEntity<>(postDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                postServiceUrl + "/posts/" + username + "/create",
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody();
    }

    public List<PostDTO> getUserPosts(String username, String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        HttpEntity<PostDTO[]> response = restTemplate.exchange(
                postServiceUrl + "/posts/" + username,
                HttpMethod.GET,
                entity,
                PostDTO[].class
        );

        return Arrays.asList(response.getBody());
    }
}
