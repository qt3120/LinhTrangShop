package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.News;

public interface NewsRepository extends JpaRepository<News, Integer> {

}
