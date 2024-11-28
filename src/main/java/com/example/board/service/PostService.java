package com.example.board.service;

import com.example.board.exception.post.PostNotFoundException;
import com.example.board.model.post.Post;
import com.example.board.model.post.PostPostRequestBody;
import com.example.board.model.post.PostUpdateRequestBody;
import com.example.board.model.entity.PostEntity;
import com.example.board.repository.PostEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {


    @Autowired
    private PostEntityRepository postEntityRepository;


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

    public Post createPost(PostPostRequestBody postPostRequestBody) {

        PostEntity newPostEntity = new PostEntity();
        newPostEntity.setBody(postPostRequestBody.body());

        var savedEntity = postEntityRepository.save(newPostEntity);

        return Post.from(savedEntity);
    }

    public Post updatePost(Long postId, PostUpdateRequestBody postUpdateRequestBody) {

        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(postId)
        );

        postEntity.setBody(postUpdateRequestBody.body());

        var savedEntity = postEntityRepository.save(postEntity);

        return Post.from(savedEntity);


    }

    public void deletePost(Long postId) {


        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(postId)
        );

        postEntityRepository.delete(postEntity);
    }
}
