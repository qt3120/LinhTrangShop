package com.user.controller;

import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.constant.ROLE;
import com.constant.SESSION_ATTR;
import com.request.UserRequest;
import com.entity.User;
import com.service.CategoryService;
import com.service.UserService;

@Controller
public class User2Controller {
    private final CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public User2Controller(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("signup")
    public String signup(Model model) {
        model.addAttribute("categoriesMenu", categoryService.list());
        model.addAttribute("user", new UserRequest());
        return "user/signup";
    }

    @GetMapping("signin")
    public String signin(Model model) {
        model.addAttribute("categoriesMenu", categoryService.list());
        model.addAttribute("user", new UserRequest());
        return "user/signin";
    }

    @GetMapping("signout")
    public String signout(Model model, HttpSession session) {
        session.removeAttribute("userLoginPage");
        return "redirect:/";
    }

    //Xử lý đăng ký
    @PostMapping("signup")
    public String postSignup(UserRequest user, BindingResult result, Model model, HttpSession session) {
        model.addAttribute("categoriesMenu", categoryService.list());
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("errorMessage", "Mật khẩu không giống nhau");
            model.addAttribute("user", user);
            return "user/signup";
        }
        User userCheck = userService.findByUsername(user.getUsername());
        if (userCheck != null) {
            model.addAttribute("errorMessage", "Username đã tồn tại");
            model.addAttribute("user", user);
            return "user/signup";
        }
        User u = new User();
        u.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
        u.setRole(ROLE.USER);
        u.setUsername(user.getUsername());
        u.setActive(1);
        userService.create(u);

        session.setAttribute(SESSION_ATTR.USER_SESSION, user);
        return "redirect:/";
    }

    @PostMapping("signin")
    public String postSignin(UserRequest user, BindingResult result, Model model, HttpSession session) {
        model.addAttribute("categoriesMenu", categoryService.list());
        User us = userService.findByUsername(user.getUsername());
        if (us == null) {
            model.addAttribute("errorMessage", "Tên tài khoản không đúng");
            model.addAttribute("user", user);
            return "user/signin";
        }
        boolean validatePassword = BCrypt.checkpw(user.getPassword(), us.getPassword());
        if (!validatePassword) {
            model.addAttribute("errorMessage", "Mật khẩu không đúng");
            model.addAttribute("user", user);
            return "user/signin";
        }
        /*
         * if (!ROLE.USER.equals(us.getRole())) { model.addAttribute("errorMessage",
         * "Bạn không có quyền truy cập"); model.addAttribute("user", user); return
         * "user/signin"; }
         */
        if (us.getActive() == 0) {
            model.addAttribute("errorMessage", "Tài khoản đã bị khóa");
            model.addAttribute("user", user);
            return "user/signin";
        }
        session.setAttribute(SESSION_ATTR.USER_SESSION, user);

        return "redirect:/";
    }

}
