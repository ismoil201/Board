package com.example.board.model.user;

import com.example.board.model.entity.UserEntity;

import java.time.ZonedDateTime;

public record LikedUser(
        Long userId,
        String username,
        String profile,
        String description,
        Long followersCount,
        Long followingsCount,
        ZonedDateTime createdDateTime,
        ZonedDateTime updateDateTime,
        Boolean isFollowing,
        Long likedPostId,
        ZonedDateTime likedDateTime
) {



    public static LikedUser from(User user, Long likedPostId, ZonedDateTime likedDateTime) {
        return  new LikedUser(
                user.userId(),
                user.username(),
                user.profile(),
                user.description(),
                user.followersCount(),
                user.followingsCount(),
                user.createdDateTime(),
                user.updateDateTime(),
                user.isFollowing(),
                likedPostId,
                likedDateTime
               );
    }
}
