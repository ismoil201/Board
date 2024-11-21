package com.example.board.repository;

import com.example.board.model.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {


}
