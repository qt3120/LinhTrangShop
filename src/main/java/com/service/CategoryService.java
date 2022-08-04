package com.service;

import java.util.List;

import com.entity.Category;

public interface CategoryService {
    List<Category> list();

    Category create(Category category);

    Category update(int id, Category category);

    Category delete(int id);

    Category findById(int id);

    List<Category> findTop5ByOrderByIdDesc();

    List<Category> findTop2ByOrderByIdDesc();
}
