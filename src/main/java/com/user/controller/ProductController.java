package com.user.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.entity.CartItem;
import com.entity.Comment;
import com.service.CategoryService;
import com.service.ProductService;

@Controller("UserProductController")
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{categoryId}")
    public String blogs(@PathVariable("categoryId") int categoryId, Model model) {
        model.addAttribute("products", productService.findByCategoryId(categoryId));
        model.addAttribute("category", categoryService.findById(categoryId));
        model.addAttribute("categoriesMenu", categoryService.list());
        return "user/product-list";
    }

    @GetMapping()
    public String products(@RequestParam(required = false) String productName, Model model) {
        model.addAttribute("products", productService.findByProductName(productName));
        model.addAttribute("categoriesMenu", categoryService.list());
        return "user/product-list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") int id, Model model, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        int soluong = 0;
        int index = -1;
        if (cart != null) {
            index = this.exists(id, cart);

        }
        if (index != -1) {
            model.addAttribute("soluong", cart.get(index).getQuantity());
        } else
            model.addAttribute("soluong", soluong);

        model.addAttribute("product", productService.findById(id));
        model.addAttribute("comment", new Comment());
        model.addAttribute("categoriesMenu", categoryService.list());
        return "user/product-detail";
    }

    private int exists(int id, List<CartItem> cart) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getProduct().getId() == id) {
                return i;
            }
        }
        return -1;
    }
}
