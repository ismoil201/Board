package com.example.board.exception.user;

import com.example.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ClientErrorException {


    public UserNotFoundException(String  username) {
        super(HttpStatus.NOT_FOUND, "User with userid " + username + " Not Found");
    }


    public UserNotFoundException() {

        super(HttpStatus.NOT_FOUND,  "User not found" );
    }
}
