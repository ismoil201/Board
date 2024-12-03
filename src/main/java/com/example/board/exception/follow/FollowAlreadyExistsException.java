package com.example.board.exception.follow;

import com.example.board.exception.ClientErrorException;
import com.example.board.model.entity.UserEntity;
import org.springframework.http.HttpStatus;

public class FollowAlreadyExistsException extends ClientErrorException {


    public FollowAlreadyExistsException(UserEntity follower , UserEntity following) {
        super(HttpStatus.CONFLICT, "Follow with follow " + follower.getUsername() + " and following "+
                        following.getUsername() + "already exists"
                );
    }


    public FollowAlreadyExistsException() {

        super(HttpStatus.CONFLICT,  "follow already exists" );
    }
}
