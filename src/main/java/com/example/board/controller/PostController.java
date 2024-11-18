package com.example.board.controller;

import com.example.board.model.Post;
import com.example.board.model.PostPostRequestBody;
import com.example.board.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {


    @Autowired private PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(){

       List<Post> posts = postService.getPostList();
        return  ResponseEntity.ok(posts);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId){

        Optional<Post> matchingPost = postService.getPostById(postId);

       return matchingPost
                .map(post -> ResponseEntity.ok(post))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    //POST =/posts
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody postPostRequestBody){

       Post post = postService.createPost(postPostRequestBody);

       return ResponseEntity.ok(post);
    }


}
