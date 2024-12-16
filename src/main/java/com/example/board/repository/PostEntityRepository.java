package com.example.board.repository;


import com.example.board.model.entity.PostEntity;
import com.example.board.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostEntityRepository  extends JpaRepository<PostEntity, Long> {

    @Query("SELECT p FROM PostEntity p ORDER BY p.postId ASC")
    List<PostEntity> findAllPostsOrdered();


    List<PostEntity>  findByUser(UserEntity user);
}
