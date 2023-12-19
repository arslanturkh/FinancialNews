package com.example.financialnews.news;

import com.example.financialnews.news.custom.CustomNewsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News>, CustomNewsRepository {
    List<News> findByTitleContainingIgnoreCase(String title);
}
