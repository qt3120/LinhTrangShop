package com.admin.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.constant.ROLE;
import com.entity.User;
import com.service.UserService;
import com.util.NewsUtil;

//Xử lý thẻ user ADMIN
@Controller
@RequestMapping("/admin/user")
public class UserController {

    private static final String USER_URL = "admin/user";
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Hiển thị danh sách người dùng
    @GetMapping()
    public String viewList(Model model, HttpSession session) {
        if (!NewsUtil.isLogin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("users", userService.list());
        return USER_URL + "/index";
    }

    @GetMapping("/add")
    public String viewAdd(Model model, HttpSession session) {
        if (!NewsUtil.isLogin(session)) {
            return "/login";
        }
        model.addAttribute("user", new User());

        return USER_URL + "/add";
    }

    @GetMapping("/active/{id}")
    public String active(Model model, HttpSession session, @PathVariable("id") int id) {
        if (!NewsUtil.isLogin(session)) {
            return "redirect:/login";
        }
        User user = userService.findById(id);
        if (user.getActive() == 0) {
            user.setActive(1);
        } else {
            user.setActive(0);
        }
        userService.create(user);
        model.addAttribute("user", new User());

        return "redirect:/admin/user";
    }

    @GetMapping("/edit/{id}")
    public String findById(@PathVariable("id") int id, Model model, HttpSession session) {
        if (!NewsUtil.isLogin(session)) {
            return "/login";
        }
        User user = userService.findById(id);
        user.setPassword("");
        model.addAttribute("item", user);
        return USER_URL + "/update";
    }

    @PostMapping()
    public String add(@Valid User user, BindingResult result, Model model, HttpSession session) {
        if (!NewsUtil.isLogin(session)) {
            return "/login";
        }
        if (result.hasErrors()) {
            return USER_URL + "/add";
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
        user.setRole(ROLE.ADMIN);
        userService.create(user);
        return "redirect:/user";
    }

    @PostMapping("/{id}")
    public String update(@Valid User user, BindingResult result, Model model, HttpSession session,
                         @PathVariable("id") int id) {
        if (!NewsUtil.isLogin(session)) {
            return "/login";
        }
        if (result.hasErrors()) {
            return USER_URL + "/update";
        }
        User u = userService.findById(id);
        u.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
        userService.create(u);
        return "redirect:/admin/user";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, Model model, HttpSession session) {
        if (!NewsUtil.isLogin(session)) {
            return "/login";
        }
        userService.delete(id);
        return "redirect:/user";
    }
}
