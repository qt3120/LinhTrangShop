package com.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.entity.Comment;
import com.service.CommentService;

@Controller("AdminCommentController")
@RequestMapping("/admin/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, Model model) {
        Comment comment = commentService.findById(id);
        commentService.delete(id);
        return "redirect:/admin/product/view/" + comment.getProduct().getId();
    }
}
