package com.example.board.controller;

import com.example.board.model.entity.UserEntity;
import com.example.board.model.post.Post;
import com.example.board.model.reply.Reply;
import com.example.board.model.user.*;
import com.example.board.service.PostService;
import com.example.board.service.ReplyService;
import com.example.board.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
   private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private ReplyService replyService;


    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String query,
                                               Authentication authentication) {

        var users = userService.getUsers(query,(UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(users);

    }


    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username ,Authentication authentication) {

        var user = userService.getUser(username,(UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);

    }

    @PatchMapping("/{username}")
    public ResponseEntity<User> upDateUser(@PathVariable String username,
                                           @RequestBody UserPatchRequestBody requestBody,
                                           Authentication authentication) {

        var user = userService.upDateUser(username,requestBody,(UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);

    }


//GET /users/{username}/posts
    @GetMapping("/{username}/posts")
    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable String username,
                                                         Authentication authentication) {

       var posts =  postService.getPostByUsername(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(posts);

    }

    @PostMapping("/{username}/follows")
    public ResponseEntity<User> follow(@PathVariable String username, Authentication authentication) {
        var user =  userService.follow(username, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(user);

    }

    @DeleteMapping("/{username}/follows")
    public ResponseEntity<User> unFollow(
            @PathVariable String username, Authentication authentication) {
        var user =  userService.unFollow(username, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(user);

    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<Follower>> getFollowersByUser(@PathVariable String username, Authentication authentication) {
        var followers =  userService.getFollowersByUsername(username,
                (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(followers);

    }

    @GetMapping("/{username}/followings")
    public ResponseEntity<List<User>> getFollowingsByUser(
            @PathVariable String username, Authentication authentication) {
        var followings =  userService.getFollowingsByUsername(username,
                (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(followings);

    }

    @GetMapping("/{username}/liked-users")
    public ResponseEntity<List<LikedUser>> getLikedUsersByUser(
            @PathVariable String username, Authentication authentication) {
        var likedUsers =
                userService.getLikedUsersByUser(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(likedUsers);

    }

    @GetMapping("/{username}/replies")
    public ResponseEntity<List<Reply>> getRepliesByUser(@PathVariable String username) {

        var replies = replyService.getRepliesByUser(username);

        return ResponseEntity.ok(replies);
    }




    @PostMapping
    public ResponseEntity<User> signUp(
            @Valid @RequestBody UserSignUpRequestBody userSignUpRequestBody) {
        var user = userService.signUp(
                userSignUpRequestBody.username(),
                userSignUpRequestBody.password()
        );
        return ResponseEntity.ok(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponse> authenticate(
            @Valid @RequestBody UserLoginRequestBody userLoginRequestBody) {
        var response = userService.authenticate(
                userLoginRequestBody.username(),
                userLoginRequestBody.password()
        );
        return ResponseEntity.ok(response);
    }
//
//    @GetMapping("")
//    public ResponseEntity<List<User>>  getUsers() {
//
//      var users =   userService.getUsers();
//
//      return ResponseEntity.ok(users);
//    }


}
