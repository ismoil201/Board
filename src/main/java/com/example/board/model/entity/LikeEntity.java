package com.example.board.model.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "\"like\"",
        indexes = {@Index(name = "like_userid_postid_idx", columnList = "userid, postid", unique = true)})

public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

   @Column
    private ZonedDateTime createdDateTime;


    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;


    @ManyToOne
    @JoinColumn(name = "postid")
    private PostEntity postEntity;

    public Long getLikeId() {
        return likeId;
    }

    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public PostEntity getPostEntity() {
        return postEntity;
    }

    public void setPostEntity(PostEntity postEntity) {
        this.postEntity = postEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeEntity that = (LikeEntity) o;
        return Objects.equals(likeId, that.likeId) && Objects.equals(createdDateTime, that.createdDateTime) && Objects.equals(user, that.user) && Objects.equals(postEntity, that.postEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(likeId, createdDateTime, user, postEntity);
    }

    public static LikeEntity of(UserEntity user, PostEntity postEntity) {
        var likeEntity = new LikeEntity();
        likeEntity.setUser(user);
        likeEntity.setPostEntity(postEntity);
        return likeEntity;
    }

    @PrePersist
    private void prePersist() {
        this.createdDateTime = ZonedDateTime.now();
    }


}
