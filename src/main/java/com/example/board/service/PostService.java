package com.example.board.service;

import com.example.board.model.Post;
import com.example.board.model.PostPostRequestBody;
import com.example.board.model.PostUpdateRequestBody;
import com.example.board.model.entity.PostEntity;
import com.example.board.repository.PostEntityRepository;
import org.apache.tomcat.jni.Pool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.HTML;
import javax.swing.text.html.parser.Entity;
import java.lang.module.ResolutionException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {


    @Autowired
    private PostEntityRepository postEntityRepository;


    public List<Post> getPostList() {

        var postEntities = postEntityRepository.findAllPostsOrdered();
        return postEntities.stream().map(Post::from).toList();
    }


    public Post getPostById(Long id) {

        var postEntity = postEntityRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        return Post.from(postEntity);
    }

    public Post createPost(PostPostRequestBody postPostRequestBody) {

        PostEntity newPostEntity =  new PostEntity();
        newPostEntity.setBody(postPostRequestBody.body());

       var savedEntity =  postEntityRepository.save(newPostEntity);

        return Post.from(savedEntity);
    }

    public Post updatePost(Long updateId, PostUpdateRequestBody postUpdateRequestBody) {

        var postEntity = postEntityRepository.findById(updateId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        postEntity.setBody(postUpdateRequestBody.body());

        var savedEntity =  postEntityRepository.save(postEntity);

      return Post.from(savedEntity);


    }

    public void deletePost(Long postId) {


        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        postEntityRepository.delete(postEntity);
    }
}
