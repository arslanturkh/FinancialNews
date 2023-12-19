package com.example.financialnews.news;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String url;

    @Column(name = "source")
    private String source;

    @Column(name = "published_date")
    private LocalDate publishedDate;

    @Transient
    private Double fullTextSearchScore;


    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Double getFullTextSearchScore() { return fullTextSearchScore; }

    public void setFullTextSearchScore(Double fullTextSearchScore) { this.fullTextSearchScore = fullTextSearchScore; }
}
