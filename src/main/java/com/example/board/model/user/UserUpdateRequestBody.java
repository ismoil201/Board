package com.example.board.model.user;

public record UserUpdateRequestBody(
        String username,
        String profile,
        String description
){
}
