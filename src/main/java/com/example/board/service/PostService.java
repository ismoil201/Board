package com.example.board.service;

import com.example.board.model.Post;
import com.example.board.model.PostPostRequestBody;
import com.example.board.model.PostUpdateRequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {


    private static  final List<Post> postList = new ArrayList<>();

    static{
        postList.add(new Post(1L,"Post1", ZonedDateTime.now()));
        postList.add(new Post(2L,"Post2", ZonedDateTime.now()));
        postList.add(new Post(3L,"Post3", ZonedDateTime.now()));
    }

    public List<Post> getPostList(){
        return postList;
    }


    public Optional<Post>  getPostById(Long id){
        return  postList.stream().filter(post -> id.equals(post.getPostId())).findFirst();
    }

    public Post createPost(PostPostRequestBody postPostRequestBody) {

       Long newPostId =  postList.stream().mapToLong(Post::getPostId).max().orElse(0L)+1;

       var newPost = new Post(newPostId, postPostRequestBody.body(), ZonedDateTime.now());

       postList.add(newPost);
       return  newPost;
    }

    public Post upDatePost(Long postId, PostUpdateRequestBody postUpdateRequestBody) {

        Optional<Post> updatePost = postList.stream().filter(post -> postId.equals(post.getPostId())).findFirst();

        if(updatePost.isPresent()){

            Post post = updatePost.get();
            post.setBody(postUpdateRequestBody.body());

            return post;
        }else {
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"Post not found");
        }

    }

    public void deletePost(Long postId) {
        Optional<Post> updatePost = postList.stream().filter(post -> postId.equals(post.getPostId())).findFirst();

        if(updatePost.isPresent()){

           postList.remove(updatePost.get());


        }else {
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"Post not found");
        }
    }
}
