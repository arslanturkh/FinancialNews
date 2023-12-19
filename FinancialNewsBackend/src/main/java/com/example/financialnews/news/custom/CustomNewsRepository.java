package com.example.financialnews.news.custom;

import com.example.financialnews.news.News;

import java.time.LocalDate;
import java.util.List;

public interface CustomNewsRepository {
    List<News> findByTitleSourceAndDate(String searchTitle, String searchSource, LocalDate date);
    List<News> findByFullTextSearch(String searchWord);
}
