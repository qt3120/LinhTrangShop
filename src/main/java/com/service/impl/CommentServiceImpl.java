package com.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.entity.Comment;
import com.repository.CommentRepository;
import com.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<Comment> list() {
        // TODO Auto-generated method stub
        return commentRepository.findAll();
    }

    @Override
    public Comment create(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(int id, Comment comment) {
        // TODO Auto-generated method stub
        comment.setId(id);
        return commentRepository.save(comment);
    }

    @Override
    public Comment delete(int id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Categry Id:" + id));
        commentRepository.delete(comment);
        return comment;
    }

    @Override
    public Comment findById(int id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
        return comment;
    }

    @Override
    public List<Comment> findTopComment(int postId) {
        // TODO Auto-generated method stub
        return commentRepository.findByProductIdOrderByIdDesc(postId);
    }
}
