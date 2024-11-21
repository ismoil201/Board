package com.example.board.controller;

import com.example.board.model.Post;
import com.example.board.model.PostPostRequestBody;
import com.example.board.model.PostUpdateRequestBody;
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

       var  matchingPost = postService.getPostById(postId);

       return ResponseEntity.ok(matchingPost);
    }


    //POST =/posts
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody postPostRequestBody){

       Post post = postService.createPost(postPostRequestBody);

       return ResponseEntity.ok(post);
    }



    @PatchMapping("/{postId}")
    public ResponseEntity<Post> upDatePost(@PathVariable Long postId,
                                           @RequestBody PostUpdateRequestBody postUpdateRequestBody){

      var upDatePost =   postService.upDatePost(postId, postUpdateRequestBody);

      return ResponseEntity.ok(upDatePost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> upDatePost(@PathVariable Long postId){

           postService.deletePost(postId);

        return ResponseEntity.noContent().build();
    }
}
