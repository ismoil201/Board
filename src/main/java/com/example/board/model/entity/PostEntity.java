package com.example.board.model.entity;


import com.example.board.model.post.Post;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "post",
        indexes = {@Index(name = "post_userid_idx", columnList = "userid")})
@SQLDelete(sql = "UPDATE \"post\" SET deleteddatetime = CURRENT_TIMESTAMP where postid = ?")
@SQLRestriction("deleteddatetime is null")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column
    private  Long repliesCount = 0L;


    @Column
    private  Long likesCount = 0L;

    @Column
    private ZonedDateTime createdDateTime;
    @Column
    private ZonedDateTime updateDateTime;
    @Column
    private ZonedDateTime deletedDateTime;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getRepliesCount() {
        return repliesCount;
    }

    public void setRepliesCount(Long repliesCount) {
        this.repliesCount = repliesCount;
    }

    public Long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Long likesCount) {
        this.likesCount = likesCount;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public ZonedDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(ZonedDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public ZonedDateTime getDeletedDateTime() {
        return deletedDateTime;
    }

    public void setDeletedDateTime(ZonedDateTime deletedDateTime) {
        this.deletedDateTime = deletedDateTime;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostEntity post = (PostEntity) o;
        return Objects.equals(postId, post.postId) && Objects.equals(body, post.body) && Objects.equals(repliesCount, post.repliesCount) && Objects.equals(likesCount, post.likesCount) && Objects.equals(createdDateTime, post.createdDateTime) && Objects.equals(updateDateTime, post.updateDateTime) && Objects.equals(deletedDateTime, post.deletedDateTime) && Objects.equals(user, post.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, body, repliesCount, likesCount, createdDateTime, updateDateTime, deletedDateTime, user);
    }

    public  static PostEntity of(String body, UserEntity user) {

        PostEntity post = new PostEntity();
        post.setBody(body);
        post.setUser(user);

        return post;
    }

    @PrePersist
    private void prePersist() {
        this.createdDateTime = ZonedDateTime.now();
        this.updateDateTime = ZonedDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.updateDateTime = ZonedDateTime.now();
    }
}
