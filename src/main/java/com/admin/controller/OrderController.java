package com.admin.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.entity.Order;
import com.model.SearchModel;
import com.service.OrderService;
import com.util.NewsUtil;

@Controller
@RequestMapping("/admin/order")
public class OrderController {

    private static final String ORDER_URL = "admin/order";

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String viewList(Model model, HttpSession session, @RequestParam(value = "status", required = false) Integer status,
                           @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        if (!NewsUtil.isLogin(session)) {
            return "redirect:/login";
        }

        List<Order> items;


        SearchModel searchModel = new SearchModel();
        if (status != null) {
            items = orderService.findByStatus(status);
            searchModel.setStatus(status);
        } else {
            items = orderService.findAll();
        }
        Page<Order> productPage = orderService.findPaginated(items, PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("items", productPage);
        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("searchItem", searchModel);

        return ORDER_URL + "/index";
    }

    @GetMapping("/{id}")
    public String viewDetail(Model model, HttpSession session, @PathVariable("id") int id) {
        if (!NewsUtil.isLogin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("item", orderService.findById(id));
        return ORDER_URL + "/detail";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, HttpSession session, @PathVariable("id") int id) {
        if (!NewsUtil.isLogin(session)) {
            return "redirect:/login";
        }
        orderService.delete(id);
        return "redirect:/admin/order";
    }

    @GetMapping("/update/{id}")
    public String update(Model model, HttpSession session, @PathVariable("id") int id) {
        if (!NewsUtil.isLogin(session)) {
            return "redirect:/login";
        }
        orderService.update(id);
        return "redirect:/admin/order";
    }

    @GetMapping("/update2/{id}")
    public String update1(Model model, HttpSession session, @PathVariable("id") int id) {
        if (!NewsUtil.isLogin(session)) {
            return "redirect:/login";
        }
        orderService.update2(id);
        return "redirect:/admin/order";
    }
}
