package com.admin.controller;

import java.util.Calendar;

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

import com.entity.Category;
import com.service.CategoryService;
import com.service.FilesStorageService;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private static final String CATEGORY_URL = "admin/category";
    private final CategoryService categoryService;
    private final FilesStorageService storageService;

    @Autowired
    public CategoryController(CategoryService categoryService, FilesStorageService storageService) {
        this.categoryService = categoryService;
        this.storageService = storageService;
    }

    // Lấy danh sách người dùng
    @GetMapping()
    public String viewList(Model model) {
        model.addAttribute("categories", categoryService.list());
        return CATEGORY_URL + "/index";
    }

    @GetMapping("/add")
    public String viewAdd(Category category) {
        return CATEGORY_URL + "/add";
    }

    @GetMapping("/edit/{id}")
    public String findById(@PathVariable("id") int id, Model model) {
        model.addAttribute("category", categoryService.findById(id));
        return CATEGORY_URL + "/update";
    }

    @PostMapping
    public String add(@Valid Category category, BindingResult result, Model model, MultipartFile imagePath) {
        if (result.hasErrors()) {
            return CATEGORY_URL + "add";
        }

        if (imagePath != null) {
            String fileName = imagePath.getOriginalFilename() + "_" + Calendar.getInstance().getTimeInMillis();
            storageService.save(imagePath, fileName);
            category.setImage(fileName);
        }
        categoryService.create(category);
        return "redirect:/category";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") int id, @Valid Category category, BindingResult result, Model model,
                         MultipartFile imagePath) {
        if (result.hasErrors()) {
            category.setId(id);
            return CATEGORY_URL + "/update";
        }
        if (imagePath != null) {
            String fileName = imagePath.getOriginalFilename() + "_" + Calendar.getInstance().getTimeInMillis();
            storageService.save(imagePath, fileName);
            category.setImage(fileName);
        } else {
            category.setImage(categoryService.findById(id).getImage());
        }
        categoryService.update(id, category);
        model.addAttribute("categories", categoryService.list());
        return CATEGORY_URL + "/index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, Model model) {
        categoryService.delete(id);
        return "redirect:/category";
    }
}
