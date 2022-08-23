package com.atguigu.jpa.dao;

import com.atguigu.jpa.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
}