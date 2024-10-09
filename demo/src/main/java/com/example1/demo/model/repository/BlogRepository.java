package com.example1.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example1.demo.model.domain.Article;

@Repository
public interface BlogRepository extends JpaRepository<Article, Long>{
}