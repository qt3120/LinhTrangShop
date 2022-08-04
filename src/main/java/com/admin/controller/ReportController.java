package com.admin.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.entity.Product;
import com.entity.Report;
import com.service.OrderService;
import com.service.ProductService;
import com.service.ReportService;

@Controller
@RequestMapping("/admin/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @GetMapping()
    public String viewList(Model model) {
        model.addAttribute("items", reportService.reportByProduct("", ""));
        model.addAttribute("date1", null);
        model.addAttribute("date2", null);
        return "admin/report/index";
    }

    @PostMapping()
    public String viewList(Model model, HttpServletRequest request) {
        String date1 = request.getParameter("date1");
        String date2 = request.getParameter("date2");

        List<Report> ls = reportService.reportByProduct(date1, date2);
        int tong = 0;
        for (Report i : ls) {
            int a = 0;
            try {
                a = Integer.parseInt(i.getAmount().trim());
            } catch (Exception e) {
                // TODO: handle exception
            }
            tong += a;
        }


        model.addAttribute("items", reportService.reportByProduct(date1, date2));
        model.addAttribute("date1", date1);
        model.addAttribute("date2", date2);
        model.addAttribute("dem", orderService.findByStatus(3).size());
        model.addAttribute("tong", tong);
        return "admin/report/index";
    }

    @GetMapping("/date")
    public String reportByDate(Model model, @RequestParam(required = false) String mode) {
        List<Product> ls = productService.list();
        List<Product> ls1 = new ArrayList<>();
        for (Product i : ls) {
            if (i.getQuantity() <= 5) {
                ls1.add(i);
            }
        }

        model.addAttribute("items", ls1);
        return "admin/report/index2";
    }
}
