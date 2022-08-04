package com.admin.controller;

import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.constant.ROLE;
import com.entity.User;
import com.service.UserService;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    // display list of employees
    @GetMapping
    public String getLogin(Model model, HttpSession session) {
        model.addAttribute("user", new User());
        return "admin/user/login";
    }

    @GetMapping("/logout")
    public String getLogout(Model model, HttpSession session) {
        model.addAttribute("user", new User());
        session.removeAttribute("username");
        return "admin/user/login";
    }

    @PostMapping()
    public String postLogin(Model model, User user, HttpSession session) {
        User us = userService.findByUsername(user.getUsername());
        if (us == null) {
            model.addAttribute("errorMessage", "Tên tài khoản không đúng!");
            return "admin/user/login";
        }
        boolean validatePassword = BCrypt.checkpw(user.getPassword(), us.getPassword());
        if (!validatePassword) {
            model.addAttribute("errorMessage", "Mật khẩu bạn vừa nhập không đúng!");
            return "admin/user/login";
        }
        if (!ROLE.ADMIN.equals(us.getRole())) {
            model.addAttribute("errorMessage", "Bạn không có quyền truy cập!");
            return "admin/user/login";
        }
        if (us.getActive() == 0) {
            model.addAttribute("errorMessage", "Tài khoản của bạn đã bị khóa. Vui lòng liên hệ với người quản trị hệ thống!");
            return "admin/user/login";
        }
        session.setAttribute("username", user.getUsername());
        return "redirect:/admin";
    }
}
