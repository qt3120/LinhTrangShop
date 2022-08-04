package com.admin.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.entity.Supply;
import com.service.SupplyService;

@Controller
@RequestMapping("/admin/supply")
public class SupplyController {

    private static final String SUPPLY_URL = "admin/supply";

    @Autowired
    private SupplyService supplyService;

    // display list of employees
    @GetMapping()
    public String viewList(Model model) {
        model.addAttribute("items", supplyService.list());
        return SUPPLY_URL + "/index";
    }

    @GetMapping("/add")
    public String viewAdd(Supply item, Model model) {
        model.addAttribute("item", item);
        return SUPPLY_URL + "/add";
    }

    @PostMapping
    public String add(@Valid Supply supply, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return SUPPLY_URL + "add";
        }

//		if (imagePath != null) {
//			String fileName = imagePath.getOriginalFilename() + "_" + Calendar.getInstance().getTimeInMillis();
//			storageService.save(imagePath, fileName);
//			category.setImage(fileName);
//		}
        supplyService.createOrUpdate(supply);
        return "redirect:/admin/supply";
    }

    @GetMapping("/edit/{id}")
    public String findById(@PathVariable("id") int id, Model model) {
        model.addAttribute("item", supplyService.findById(id));
        return SUPPLY_URL + "/update";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") int id, @Valid Supply item, BindingResult result, Model model,
                         MultipartFile imagePath) {
        item.setId(id);
        if (result.hasErrors()) {
            return SUPPLY_URL + "/update";
        }
        supplyService.createOrUpdate(item);
        return "redirect:/admin/supply";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, Model model) {
        supplyService.delete(id);
        return "redirect:/admin/supply";
    }
}
