package com.example.board.controller;

import com.example.board.model.Post;
import com.example.board.model.PostPostRequestBody;
import com.example.board.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/posts")
@RestController
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(){


        var posts = postService.getPosts();

        return ResponseEntity.ok(posts);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable Long postId){



       var post1 =  postService.getPostById(postId);


       return post1
                .map(post -> ResponseEntity.ok(post))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public  ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody postPostRequestBody){

        var newPost = postService.createPost(postPostRequestBody);

        return ResponseEntity.ok(newPost);

    }
}
