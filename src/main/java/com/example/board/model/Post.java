package com.example.board.model;

import com.example.board.model.entity.PostEntity;

import java.time.ZonedDateTime;
import java.util.Objects;

public record Post(
        Long postId,
        String body,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime,
        ZonedDateTime deletedDateTime
) {

    public static Post from(PostEntity postEntity) {
        return new Post(postEntity.getPostId(),
                postEntity.getBody(),
                postEntity.getCreatedDateTime(),
                postEntity.getUpdatedDateTime(),
                postEntity.getDeletedDateTime());
    }

}
