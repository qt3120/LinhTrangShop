package com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entity.News;
import com.repository.NewsRepository;
import com.service.NewsService;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Override
    public List<News> findAll() {
        // TODO Auto-generated method stub
        return newsRepository.findAll();
    }

    @Override
    public News create(News news) {
        return newsRepository.save(news);
    }

    @Override
    public News delete(int id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid News Id:" + id));
        newsRepository.delete(news);
        return news;
    }

    @Override
    public News findById(int id) {
        // TODO Auto-generated method stub
        return newsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid News Id:" + id));
    }

    @Override
    public List<News> list() {
        // TODO Auto-generated method stub
        return newsRepository.findAll();
    }


}
