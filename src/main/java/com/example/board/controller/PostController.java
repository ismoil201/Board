package com.example.board.controller;

import com.example.board.model.entity.UserEntity;
import com.example.board.model.post.Post;
import com.example.board.model.post.PostPostRequestBody;
import com.example.board.model.post.PostUpdateRequestBody;
import com.example.board.model.user.LikedUser;
import com.example.board.service.PostService;
import com.example.board.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {


    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(Authentication authentication) {
        logger.info("GET /api/v1/posts");
        List<Post> posts = postService.getPostList((UserEntity) authentication.getPrincipal() );
        return ResponseEntity.ok(posts);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostByPostId(@PathVariable Long postId, Authentication authentication) {

        logger.info("GET api/v1/posts/{postId}");
        var post = postService.getPostById(postId,(UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(post);
    }

    @GetMapping("/{postId}/liked-users")
    public ResponseEntity<List<LikedUser>> getLikedUsersByPostId
            (@PathVariable Long postId, Authentication authentication) {

       var likedUsers =
               userService.getLikedUsersByPostId(postId, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(likedUsers);
    }


    //POST =/posts
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody postPostRequestBody
    , Authentication authentication)
    {

        logger.info("POST api/v1/posts");
        Post post = postService.createPost(postPostRequestBody, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(post);
    }


    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long postId,
                                           @RequestBody PostUpdateRequestBody postUpdateRequestBody,
                                           Authentication authentication) {
        logger.info("PATCH api/v1/posts/{postId}");
        Post updatePost = postService.updatePost(postId, postUpdateRequestBody, (UserEntity) authentication.getPrincipal());


        return ResponseEntity.ok(updatePost);
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, Authentication authentication) {

        logger.info("DELETE api/v1/posts/{postId}");
        postService.deletePost(postId, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.noContent().build();

    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<Post> toggleLike(@PathVariable Long postId, Authentication authentication) {

      var post =   postService.toggleLike(postId, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(post);

    }

}
