package ru.nand.eurekaclientposts.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nand.eurekaclientposts.models.Post;
import ru.nand.eurekaclientposts.repositories.PostRepository;
import ru.nand.eurekaclientposts.util.PostNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll(){
        return postRepository.findAll();
    }

    public Post findOne(int id){
        Optional<Post> foundPost = postRepository.findById(id);
        return foundPost.orElseThrow(PostNotFoundException::new);
    }

    public List<Post> findByOwner(String owner){
        return postRepository.findByOwner(owner);
    }

    @Transactional
    public void save(Post post){
        post.setDateOfPublication(LocalDateTime.now());
        postRepository.save(post);
    }


}
