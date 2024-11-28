package com.example.board.model.user;

import com.example.board.model.entity.UserEntity;

import java.time.ZonedDateTime;

public record  User(
        Long userId,
        String username,
        String profile,
        String description,
        ZonedDateTime createdDateTime,
        ZonedDateTime updateDateTime
) {

    public static User from(UserEntity userEntity) {
        return  new User(
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getProfile(),
                userEntity.getDescription(),
                userEntity.getCreatedDateTime(),
                userEntity.getUpdatedDateTime());
    }
}
