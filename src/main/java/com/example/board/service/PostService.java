package com.example.board.service;

import com.example.board.exception.post.PostNotFoundException;
import com.example.board.exception.user.UserNotAllowedException;
import com.example.board.exception.user.UserNotFoundException;
import com.example.board.model.entity.LikeEntity;
import com.example.board.model.entity.UserEntity;
import com.example.board.model.post.Post;
import com.example.board.model.post.PostPostRequestBody;
import com.example.board.model.post.PostUpdateRequestBody;
import com.example.board.model.entity.PostEntity;
import com.example.board.repository.LikeEntityRepository;
import com.example.board.repository.PostEntityRepository;
import com.example.board.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {


    @Autowired
    private PostEntityRepository postEntityRepository;
    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private LikeEntityRepository likeEntityRepository;


    public List<Post> getPostList(UserEntity currentUser) {

        var postEntities = postEntityRepository.findAllPostsOrdered();
        return postEntities.stream().map(
                postEntity -> getPostWithLikingStatus(postEntity, currentUser)
        ).toList();
    }


    private Post getPostWithLikingStatus(PostEntity postEntity, UserEntity currentUser) {
        var isLiking = likeEntityRepository.findByUserAndPostEntity(currentUser, postEntity).isPresent();
        return Post.from(postEntity, isLiking);
    }
    public Post getPostById(Long postId, UserEntity currentUser) {

        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(postId)
        );

        return getPostWithLikingStatus(postEntity, currentUser);
    }

    public Post createPost(PostPostRequestBody postPostRequestBody, UserEntity userEntity) {


        var savedEntity = postEntityRepository.save(
                PostEntity.of(postPostRequestBody.body(), userEntity)
        );

        return Post.from(savedEntity);
    }

    public Post updatePost(Long postId, PostUpdateRequestBody postUpdateRequestBody,
                           UserEntity userEntity) {

        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(postId)
        );

        if (!postEntity.getUser().equals(userEntity)) {
            throw new UserNotAllowedException();
        }
        postEntity.setBody(postUpdateRequestBody.body());

        var savedEntity = postEntityRepository.save(postEntity);

        return Post.from(savedEntity);


    }

    public void deletePost(Long postId, UserEntity userEntity) {


        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(postId)
        );
        if (!postEntity.getUser().equals(userEntity)) {
            throw new UserNotAllowedException();
        }

        postEntityRepository.delete(postEntity);
    }

    public List<Post> getPostByUsername(String username, UserEntity currentUser) {
        var userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        var postEntities = postEntityRepository.findByUser(userEntity);

        return postEntities.stream().map(
                postEntity -> getPostWithLikingStatus(postEntity, currentUser)
        ).toList();
    }

    @Transactional
    public Post toggleLike(Long postId, UserEntity currentUser) {
        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(postId));

        var likeEntity = likeEntityRepository.findByUserAndPostEntity(currentUser, postEntity);


        if (likeEntity.isPresent()) {
            likeEntityRepository.delete(likeEntity.get());
            postEntity.setLikesCount(Math.max(0, postEntity.getLikesCount() - 1));
            return Post.from(postEntityRepository.save(postEntity), false);
        } else {

            likeEntityRepository.save(LikeEntity.of(currentUser, postEntity));
            postEntity.setLikesCount(postEntity.getLikesCount() + 1);
            return Post.from(postEntityRepository.save(postEntity), true);
        }

    }
}
