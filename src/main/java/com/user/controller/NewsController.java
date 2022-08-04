package com.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.entity.Comment;
import com.service.CategoryService;
import com.service.NewsService;
import com.service.ProductService;

@Controller("UserNewsController")
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private NewsService newsService;

    @GetMapping
    public String news(Model model) {

        model.addAttribute("categoriesMenu", categoryService.list());
        model.addAttribute("items", newsService.findAll());
        return "user/news/index";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") int id, Model model) {
//		model.addAttribute("product", productService.findById(id));
//		model.addAttribute("topComment", commentService.findTopComment(id).size() > 0 ? commentService.findTopComment(id) : new Comment());
        model.addAttribute("item", newsService.findById(id));
        model.addAttribute("categoriesMenu", categoryService.list());
        return "user/news/detail";
    }
}
