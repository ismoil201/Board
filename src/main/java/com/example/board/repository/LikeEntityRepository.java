package com.example.board.repository;


import com.example.board.model.entity.LikeEntity;
import com.example.board.model.entity.PostEntity;
import com.example.board.model.entity.ReplyEntity;
import com.example.board.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeEntityRepository extends JpaRepository<LikeEntity, Long> {


    List<LikeEntity>  findByUser(UserEntity user);

//    List<LikeEntity>  findByPost(PostEntity post);
    @Query("SELECT r FROM LikeEntity r WHERE r.postEntity = :postEntity")
    List<LikeEntity> findByPost(@Param("postEntity") PostEntity postEntity);

    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);
}
