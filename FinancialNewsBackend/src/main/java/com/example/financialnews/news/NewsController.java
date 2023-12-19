package com.example.financialnews.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/news")
public class NewsController {
    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    // Get all articles
    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {
        List<News> news = newsService.findAll();
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    // Filter articles by title, source and date
    @GetMapping("/search")
    public ResponseEntity<List<News>> filterArticles(
            @RequestParam(required = false) String searchWord,
            @RequestParam(required = false) String sourceSearch,
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<News> articles = newsService.findByTitleAndSource(searchWord, sourceSearch, date);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }
}
