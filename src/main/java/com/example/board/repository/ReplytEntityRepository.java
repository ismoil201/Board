package com.example.board.repository;


import com.example.board.model.entity.PostEntity;
import com.example.board.model.entity.ReplyEntity;
import com.example.board.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplytEntityRepository extends JpaRepository<ReplyEntity, Long> {


    List<ReplyEntity>  findByUser(UserEntity user);

//    List<ReplyEntity>  findByPost(PostEntity post);
    @Query("SELECT r FROM ReplyEntity r WHERE r.postEntity = :postEntity")
    List<ReplyEntity> findByPost(@Param("postEntity") PostEntity postEntity);

}
