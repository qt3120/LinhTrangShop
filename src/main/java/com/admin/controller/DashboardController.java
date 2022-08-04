package com.admin.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.service.CategoryService;
import com.service.NewsService;
import com.service.ProductService;
import com.util.NewsUtil;

@Controller
@RequestMapping("/admin")
public class DashboardController {
    private final NewsService postService;
    private final CategoryService categoryService;
    private final ProductService productService;

    @Autowired
    public DashboardController(NewsService postService, CategoryService categoryService, ProductService productService) {
        this.postService = postService;
        this.categoryService = categoryService;
        this.productService = productService;
    }

    // display list of employees
    @GetMapping
    public String viewHomePage(Model model, HttpSession session) {
//        model.addAttribute("listEmployees", employeeService.getAllEmployees());
        if (!NewsUtil.isLogin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("totalCategory", categoryService.list().size());
        model.addAttribute("totalPost", postService.list().size());
        model.addAttribute("totalProduct", productService.list().size());
        return "admin/index";
    }
}
