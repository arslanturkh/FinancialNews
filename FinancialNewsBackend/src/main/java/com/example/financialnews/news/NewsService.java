package com.example.financialnews.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class NewsService {
    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<News> findAll() {
        return newsRepository.findAll();
    }

    public List<News> findByTitleAndSource(String title, String source, LocalDate date){
        List<News> newsToReturn = new ArrayList<News>();

        if (title.trim().equals("") && source.trim().equals("") && date == null){
            newsToReturn = newsRepository.findAll();
        }else {
            newsToReturn = newsRepository.findByTitleSourceAndDate(title, source, date);

            if (!title.trim().equals("") && newsToReturn.isEmpty()){
                // Bonus: Full text search
                newsToReturn = newsRepository.findByFullTextSearch(title);
            }
        }

        return newsToReturn;
    }
}
