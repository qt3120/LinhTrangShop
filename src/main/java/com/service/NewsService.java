package com.service;

import java.util.List;


import com.entity.News;

public interface NewsService {
    List<News> findAll();

    News create(News category);

    News delete(int id);

    News findById(int id);

    List<News> list();
}
