package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findTopByProductIdOrderByIdDesc(int postId);

    List<Comment> findByProductIdOrderByIdDesc(int postId);
}
