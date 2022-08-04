package com.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.entity.Comment;
import com.service.CategoryService;
import com.service.CommentService;
import com.service.ProductService;

@Controller
@RequestMapping("/")
public class HomeController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final CommentService commentService;

    @Autowired
    public HomeController(ProductService postService, CategoryService categoryService, CommentService commentService) {
        this.productService = postService;
        this.categoryService = categoryService;
        this.commentService = commentService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("productPins", productService.findByPin(true));
        model.addAttribute("postNewest", productService.findNewest());
        model.addAttribute("categories", categoryService.findTop5ByOrderByIdDesc());
        model.addAttribute("categories2", categoryService.findTop2ByOrderByIdDesc());
        model.addAttribute("categoriesMenu", categoryService.list());
        return "user/index";
    }
}
