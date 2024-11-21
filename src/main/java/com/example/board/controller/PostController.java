package com.example.board.controller;

import com.example.board.model.Post;
import com.example.board.model.PostPostRequestBody;
import com.example.board.model.PostUpdateRequestBody;
import com.example.board.model.entity.PostEntity;
import com.example.board.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(){

       List<Post> posts = postService.getPostList();
        return  ResponseEntity.ok(posts);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId){

       var post = postService.getPostById(postId);

       return ResponseEntity.ok(post);
    }


    //POST =/posts
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody postPostRequestBody){



       Post post = postService.createPost(postPostRequestBody);

       return ResponseEntity.ok(post);
    }


    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long postId,
                                           @RequestBody PostUpdateRequestBody postUpdateRequestBody){

       Post updatePost = postService.updatePost(postId, postUpdateRequestBody);


       return  ResponseEntity.ok(updatePost);
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<Post> deletePost(@PathVariable Long postId){

        postService.deletePost(postId);

        return ResponseEntity.noContent().build();

    }

}
