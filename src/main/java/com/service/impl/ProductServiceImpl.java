package com.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.entity.Product;
import com.entity.Supply;
import com.repository.ProductRepository;
import com.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository postRepository) {
        this.productRepository = postRepository;
    }

    @Override
    public List<Product> list() {
        // TODO Auto-generated method stub
        return productRepository.findAllByOrderByIdDesc();
    }

    @Override
    public Product create(Product post) {
        return productRepository.save(post);
    }

    @Override
    public Product update(int id, Product post) {
        // TODO Auto-generated method stub
        post.setId(id);
        return productRepository.save(post);
    }

    @Override
    public Product delete(int id) {
        Product post = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Categry Id:" + id));
        productRepository.delete(post);
        return post;
    }

    @Override
    public Product findById(int id) {
        Product post = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        return post;
    }

    @Override
    public List<Product> findNewest() {
        return productRepository.findTop6ByPinOrderByIdDesc(true);
    }

    @Override
    public List<Product> findByPin(boolean pin) {
        return productRepository.findByPin(pin);
    }

    @Override
    public List<Product> findByCategoryId(int categoryId) {
        // TODO Auto-generated method stub
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Supply> findBySupplyId(int supplyId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Product> findByProductName(String productName) {
        // TODO Auto-generated method stub
        return productRepository.findByProductNameLike(productName);
    }

    @Override
    public Page<Product> findPaginated(List<Product> products, Pageable pageable) {

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Product> list;

        if (products.size() < startItem) {
            list = new ArrayList<>();
        } else {
            int toIndex = Math.min(startItem + pageSize, products.size());
            list = products.subList(startItem, toIndex);
        }

        Page<Product> bookPage = new PageImpl<Product>(list, PageRequest.of(currentPage, pageSize), products.size());

        return bookPage;
    }

}
