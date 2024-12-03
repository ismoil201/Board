package com.example.board.exception.follow;

import com.example.board.exception.ClientErrorException;
import com.example.board.model.entity.UserEntity;
import org.springframework.http.HttpStatus;

public class FollowNotFoundException extends ClientErrorException {


    public FollowNotFoundException() {

        super(HttpStatus.NOT_FOUND, "Follow not found");
    }


    public FollowNotFoundException(UserEntity follower , UserEntity following) {
        super(HttpStatus.NOT_FOUND,
                "Follow with follow " + follower.getUsername()
                        + " and following "+ following.getUsername() + "not found"
        );
    }




}
