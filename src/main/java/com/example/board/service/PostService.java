package com.example.board.service;

import com.example.board.model.Post;
import com.example.board.model.PostPostRequestBody;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {


    public final static List<Post> posts  = new ArrayList<>();

    static{


        posts.add(new Post(1L,"Post1", ZonedDateTime.now()));
        posts.add(new Post(2L,"Post2", ZonedDateTime.now()));
        posts.add(new Post(3L,"Post3", ZonedDateTime.now()));

    }



    public List<Post> getPosts(){
        return posts;
    }


    public Optional<Post> getPostById(Long id){

       Optional<Post> p = posts.stream().filter(post -> id.equals(post.getPostId())).findFirst();
       return p;
    }

    public Post createPost(PostPostRequestBody postPostRequestBody) {


        Long newId =  posts.stream().map(Post::getPostId).max(Long::compare).orElse(0L)+1L;

        Post post =  new Post(newId, postPostRequestBody.body() , ZonedDateTime.now());

        posts.add(post);

        return post;
    }
}
