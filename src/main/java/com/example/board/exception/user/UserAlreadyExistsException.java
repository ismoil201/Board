package com.example.board.exception.user;

import com.example.board.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ClientErrorException {


    public UserAlreadyExistsException(String  username) {
        super(HttpStatus.CONFLICT, "User with userid " + username + "  already exists");
    }


    public UserAlreadyExistsException() {

        super(HttpStatus.CONFLICT,  "User already exists" );
    }
}
