package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findTop5ByOrderByIdDesc();

    List<Category> findTop2ByOrderByIdDesc();
}
