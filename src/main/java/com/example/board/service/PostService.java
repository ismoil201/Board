package com.example.board.service;

import com.example.board.exception.post.PostNotFoundException;
import com.example.board.exception.user.UserNotAllowedException;
import com.example.board.exception.user.UserNotFoundException;
import com.example.board.model.entity.UserEntity;
import com.example.board.model.post.Post;
import com.example.board.model.post.PostPostRequestBody;
import com.example.board.model.post.PostUpdateRequestBody;
import com.example.board.model.entity.PostEntity;
import com.example.board.repository.PostEntityRepository;
import com.example.board.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {


    @Autowired
    private PostEntityRepository postEntityRepository;
    @Autowired
    private UserEntityRepository userEntityRepository;


    public List<Post> getPostList() {

        var postEntities = postEntityRepository.findAllPostsOrdered();
        return postEntities.stream().map(Post::from).toList();
    }


    public Post getPostById(Long postId) {

        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(postId)
        );
        return Post.from(postEntity);
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

    public List<Post> getPostByUsername(String username) {
        var userEntity = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        var postEntity = postEntityRepository.findByUser(userEntity);

        return postEntity.stream().map(Post::from).toList();
    }
}
