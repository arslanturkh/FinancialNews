package com.example.financialnews.news.custom;

import com.example.financialnews.news.News;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomNewsRepositoryImpl extends SimpleJpaRepository<News, Long> implements CustomNewsRepository {
    private final EntityManager entityManager;

    @Autowired
    public CustomNewsRepositoryImpl(EntityManager entityManager) {
        super(JpaEntityInformationSupport.getEntityInformation(News.class, entityManager), entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public List<News> findByTitleSourceAndDate(String searchTitle, String searchSource, LocalDate date) {
        return findAll((root, query, criteriaBuilder) -> {
            Predicate finalPredicate = criteriaBuilder.conjunction();

            if (!searchTitle.trim().equals("")) {
                Predicate titlePredicate = criteriaBuilder.equal(root.get("title"), searchTitle);
                finalPredicate = criteriaBuilder.and(finalPredicate, titlePredicate);
            }

            if (!searchSource.trim().equals("")) {
                Predicate sourcePredicate = criteriaBuilder.equal(root.get("source"), searchSource);
                finalPredicate = criteriaBuilder.and(finalPredicate, sourcePredicate);
            }

            if (date != null) {
                Predicate datePredicate = criteriaBuilder.equal(root.get("publishedDate"), date);
                finalPredicate = criteriaBuilder.and(finalPredicate, datePredicate);
            }

            return finalPredicate;
        });
    }

    @Override
    public List<News> findByFullTextSearch(String searchWord) {
        String sql = "SELECT *, ts_rank(to_tsvector('english', title), plainto_tsquery('english', :searchWord)) AS rank " +
                "FROM news " +
                "WHERE to_tsvector('english', title) @@ plainto_tsquery('english', :searchWord) " +
                "ORDER BY rank DESC";

        Query nativeQuery = entityManager.createNativeQuery(sql);
        nativeQuery.setParameter("searchWord", searchWord);

        List<Object[]> rows = nativeQuery.getResultList();
        List<News> newsList = new ArrayList<>();

        try {
            newsList = rows.stream().map(row -> {
                News news = new News();
                news.setId((long) ((Number) row[0]).intValue());
                news.setTitle((String) row[1]);
                news.setUrl((String) row[2]);
                news.setSource((String) row[3]);
                news.setPublishedDate(((java.sql.Date) row[4]).toLocalDate());
                news.setFullTextSearchScore(((Number) row[5]).doubleValue());

                return news;
            }).collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
        }


        return newsList;
    }
}
