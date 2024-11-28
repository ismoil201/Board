package com.example.board.controller;

import com.example.board.model.user.User;
import com.example.board.model.user.UserSignUpRequestBody;
import com.example.board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired UserService userService;


    @PostMapping
    public ResponseEntity<User> signUp(@RequestBody UserSignUpRequestBody userSignUpRequestBody){
      var user =   userService.signUp(
              userSignUpRequestBody.username(),
              userSignUpRequestBody.password()
      );
      return ResponseEntity.ok(user);
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
