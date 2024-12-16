package com.example.board.repository;


import com.example.board.model.entity.FollowEntity;
import com.example.board.model.entity.LikeEntity;
import com.example.board.model.entity.PostEntity;
import com.example.board.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowEntityRepository extends JpaRepository<FollowEntity, Long> {


    // FollowEntity metodlari
    @Query("SELECT l FROM FollowEntity l WHERE l.follower = :follower")
    List<FollowEntity> findByFollower(@Param("follower") UserEntity follower); // Followerga tegishli follow-lar

    @Query("SELECT l FROM FollowEntity l WHERE l.following = :following")
    List<FollowEntity> findByFollowing(@Param("following") UserEntity following); // Followingga tegishli follow-lar


    @Query("SELECT l FROM FollowEntity l WHERE l.follower = :follower AND l.following = :following")
    Optional<FollowEntity> findByFollowerAndFollowing(@Param("follower") UserEntity follower,
                                                      @Param("following") UserEntity following);




}
