package com.example.board.service;

import com.example.board.model.Post;
import com.example.board.model.PostPostRequestBody;
import com.example.board.model.PostUpdateRequestBody;
import com.example.board.model.entity.PostEntity;
import com.example.board.repository.PostEntityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {


    private PostEntityRepository postEntityRepository;


    public List<Post> getPostList(){
        return postEntityRepository.findAll().stream().map(Post::from).toList();
    }


    public Post  getPostById(Long id){
        return  Post.from(postEntityRepository.findById(id).get());
    }

    public Post createPost(PostPostRequestBody postPostRequestBody) {


        var newPost = postEntityRepository.save(new PostEntity(postPostRequestBody.body()));

       return Post.from(newPost) ;
    }

    public Post upDatePost(Long postId, PostUpdateRequestBody postUpdateRequestBody) {


       var postById =  postEntityRepository.findById(postId);

        if(postById.isPresent()){

            postById.get().setBody(postUpdateRequestBody.body());

            return Post.from(postEntityRepository.save(postById.get()));
        }else {
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"Post not found");
        }

    }

    public void deletePost(Long postId) {

        postEntityRepository.deleteById(postId);

    }
}
