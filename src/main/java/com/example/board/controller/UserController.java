package com.example.board.controller;

import com.example.board.model.entity.UserEntity;
import com.example.board.model.post.Post;
import com.example.board.model.user.*;
import com.example.board.service.PostService;
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


    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String query) {

        var users = userService.getUsers(query);
        return ResponseEntity.ok(users);

    }


    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {

        var user = userService.getUser(username);
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
    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable String username) {

       var posts =  postService.getPostByUsername(username);
        return ResponseEntity.ok(posts);

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
