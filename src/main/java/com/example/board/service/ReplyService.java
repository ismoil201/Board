package com.example.board.service;

import com.example.board.exception.post.PostNotFoundException;
import com.example.board.exception.reply.ReplyNotFoundException;
import com.example.board.exception.user.UserNotAllowedException;
import com.example.board.exception.user.UserNotFoundException;
import com.example.board.model.entity.PostEntity;
import com.example.board.model.entity.ReplyEntity;
import com.example.board.model.entity.UserEntity;
import com.example.board.model.post.Post;
import com.example.board.model.post.PostUpdateRequestBody;
import com.example.board.model.reply.Reply;
import com.example.board.model.reply.ReplyReplyRequestBody;
import com.example.board.model.reply.ReplyUpdateRequestBody;
import com.example.board.repository.PostEntityRepository;
import com.example.board.repository.ReplytEntityRepository;
import com.example.board.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReplyService {


    @Autowired
    private ReplytEntityRepository replytEntityRepository;

    @Autowired
    private PostEntityRepository postEntityRepository;
    @Autowired
    private UserEntityRepository userEntityRepository;


    public List<Post> getPostList() {

        var postEntities = postEntityRepository.findAllPostsOrdered();
        return postEntities.stream().map(Post::from).toList();
    }

    public List<Reply> getRepliesByPostId(Long postId) {
        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(postId)
        );
        var replyEntities = replytEntityRepository.findByPost(postEntity);

        return replyEntities.stream().map(Reply::from).toList();
    }


    @Transactional
    public Reply createReply(Long postId, ReplyReplyRequestBody replyReplyRequestBody, UserEntity currentUser
    ) {

        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(postId));


        var replyEntity = replytEntityRepository
                .save(ReplyEntity.of(replyReplyRequestBody.body(), currentUser, postEntity));
        postEntity.setRepliesCount(postEntity.getRepliesCount() + 1);
        return Reply.from(replyEntity);
    }

    public Reply updateReply(Long postId, Long replyId, ReplyUpdateRequestBody replyUpdateRequestBody,
                             UserEntity currentUser) {

        postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        var replyEntity =
                replytEntityRepository.findById(replyId).orElseThrow(() -> new ReplyNotFoundException(replyId));

        if (!replyEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }
        replyEntity.setBody(replyUpdateRequestBody.body());


        return Reply.from(replytEntityRepository.save(replyEntity));


    }

    @Transactional
    public void deleteReply(Long postId, Long replyId, UserEntity currentUser) {

       var postEntity =
               postEntityRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));


        var replyEntity =
                replytEntityRepository.findById(replyId).orElseThrow(() -> new ReplyNotFoundException(replyId));

        if (!replyEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        replytEntityRepository.delete(replyEntity);

        postEntity.setRepliesCount(Math.max(0,postEntity.getRepliesCount() - 1));
        postEntityRepository.save(postEntity);
    }


}
