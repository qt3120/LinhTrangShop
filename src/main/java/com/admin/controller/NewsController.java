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

import com.entity.News;
import com.service.CategoryService;
import com.service.FilesStorageService;
import com.service.NewsService;

@Controller
@RequestMapping("/admin/news")
public class NewsController {

    private static final String NEWS_URL = "admin/news";
    private final CategoryService categoryService;
    private final FilesStorageService storageService;

    @Autowired
    private NewsService newsService;

    @Autowired
    public NewsController(CategoryService categoryService, FilesStorageService storageService) {
        this.categoryService = categoryService;
        this.storageService = storageService;
    }

    // display list of employees
    @GetMapping()
    public String viewList(Model model) {
        model.addAttribute("items", newsService.findAll());
        return NEWS_URL + "/index";
    }

    @GetMapping("/add")
    public String viewAdd(Model model) {
        model.addAttribute("item", new News());
        return NEWS_URL + "/add";
    }

    @PostMapping
    public String add(@Valid News news, BindingResult result, Model model, MultipartFile imagePath) {
        if (result.hasErrors()) {
            model.addAttribute("item", news);
            return NEWS_URL + "/add";
        }

        if (imagePath != null) {
            String fileName = imagePath.getOriginalFilename() + "_" + Calendar.getInstance().getTimeInMillis();
            storageService.save(imagePath, fileName);
            news.setImage(fileName);
        }
        newsService.create(news);
        return "redirect:/" + NEWS_URL;
    }

    @GetMapping("/edit/{id}")
    public String findById(@PathVariable("id") int id, Model model) {
        model.addAttribute("item", newsService.findById(id));
        return NEWS_URL + "/update";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") int id, @Valid News news, BindingResult result, Model model,
                         MultipartFile imagePath) {
        news.setId(id);
        if (result.hasErrors()) {
            return NEWS_URL + "/update";
        }
        if (imagePath != null) {
            String fileName = imagePath.getOriginalFilename() + "_" + Calendar.getInstance().getTimeInMillis();
            storageService.save(imagePath, fileName);
            news.setImage(fileName);
        } else {
            news.setImage(newsService.findById(id).getImage());
        }
        newsService.create(news);
        return "redirect:/" + NEWS_URL;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, Model model) {
        newsService.delete(id);
        return "redirect:/" + NEWS_URL;
    }
}
