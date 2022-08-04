package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findTop6ByOrderByIdDesc();

    List<Product> findByPin(boolean pin);

    List<Product> findTop6ByPinOrderByIdDesc(boolean pin);

    List<Product> findByCategoryIdAndPin(int categoryId, boolean pin);

    List<Product> findByCategoryId(int categoryId);

    List<Product> findBySupplyId(int categoryId);

    List<Product> findByProductNameLike(String productName);

    List<Product> findAllByOrderByIdDesc();
}
