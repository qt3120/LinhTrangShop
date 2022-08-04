package com.user.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.constant.SESSION_ATTR;
import com.entity.CartItem;
import com.entity.Order;
import com.entity.User;
import com.request.UserRequest;
import com.service.CategoryService;
import com.service.OrderService;
import com.service.UserService;
import com.util.NewsUtil;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String index(Model model, HttpSession session) {
        if (!NewsUtil.isUserLogin(session)) {
            return "redirect:/signin";
        }
        UserRequest user = (UserRequest) session.getAttribute(SESSION_ATTR.USER_SESSION);
        model.addAttribute("categoriesMenu", categoryService.list());
        User a = userService.findByUsername(user.getUsername());
        Order order = new Order();
        order.setAddress(a.getAddress());
        order.setEmail(a.getEmail());
        order.setName(a.getName());
        order.setPhoneNumber(a.getPhoneNumber());
        model.addAttribute("item", order);

        return "user/checkout/index";
    }

    @PostMapping
    public String postCheckout(Model model, Order item, HttpSession session) {
        if (!NewsUtil.isUserLogin(session)) {
            return "redirect:/signin";
        }
        UserRequest user = (UserRequest) session.getAttribute(SESSION_ATTR.USER_SESSION);
        User a = userService.findByUsername(user.getUsername());
        a.setName(item.getName());
        a.setEmail(item.getEmail());
        a.setPhoneNumber(item.getPhoneNumber());
        a.setAddress(item.getAddress());
        userService.update(a.getId(), a);
        orderService.order(item, user.getUsername(), (List<CartItem>) session.getAttribute("cart"));
        model.addAttribute("categoriesMenu", categoryService.list());
        session.removeAttribute("cart");
        return "user/checkout/success";
    }
}
