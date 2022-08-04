package com.user.controller;

import java.util.Calendar;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.constant.SESSION_ATTR;
import com.entity.Comment;
import com.entity.User;
import com.request.UserRequest;
import com.service.CommentService;
import com.service.ProductService;
import com.service.UserService;

@Controller
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;
    private final ProductService postService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService, ProductService postService) {
        this.commentService = commentService;
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping("/{productId}")
    public String add(@Valid Comment comment, Model model, @PathVariable("productId") int productId,
                      HttpSession session) {
        UserRequest userSession = (UserRequest) session.getAttribute(SESSION_ATTR.USER_SESSION);
        if (userSession == null) {
            return "redirect:/signin";
        }
        User user = userService.findByUsername(userSession.getUsername());
        comment.setUser(user);
        comment.setProduct(postService.findById(productId));
        comment.setCreatedAt(Calendar.getInstance().getTime());
        commentService.create(comment);
        return "redirect:/products/detail/" + productId;
    }
}
