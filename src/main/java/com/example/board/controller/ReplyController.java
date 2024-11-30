package com.example.board.controller;

import com.example.board.model.entity.UserEntity;
import com.example.board.model.post.Post;
import com.example.board.model.post.PostPostRequestBody;
import com.example.board.model.post.PostUpdateRequestBody;
import com.example.board.model.reply.Reply;
import com.example.board.model.reply.ReplyReplyRequestBody;
import com.example.board.model.reply.ReplyUpdateRequestBody;
import com.example.board.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/replies")//TODO ertaga shuni test qilish POSTMANDA
public class ReplyController {


    @Autowired
    private ReplyService replyService;

    @GetMapping
    public ResponseEntity<List<Reply>> getRepliesByPostId(@PathVariable Long postId) {
        var replies = replyService.getRepliesByPostId(postId);
        return ResponseEntity.ok(replies);
    }


    @PostMapping
    public ResponseEntity<Reply> createReply(
            @PathVariable Long postId
            , @RequestBody ReplyReplyRequestBody replyReplyRequestBody
            , Authentication authentication) {

        var reply = replyService.createReply(postId, replyReplyRequestBody, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(reply);
    }


    @PatchMapping("/{replyId}")
    public ResponseEntity<Reply> updatePost(
            @PathVariable Long postId,
            @PathVariable Long replyId,
            @RequestBody ReplyUpdateRequestBody replyUpdateRequestBody,
            Authentication authentication) {

        var updateReply =
                replyService
                        .updateReply(postId, replyId, replyUpdateRequestBody, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(updateReply);
    }


    @DeleteMapping("/{replyId}")
    public ResponseEntity<Reply> deletePost(
            @PathVariable Long postId,
            @PathVariable Long replyId, Authentication authentication) {


        replyService.deleteReply(postId, replyId, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.noContent().build();

    }

}
