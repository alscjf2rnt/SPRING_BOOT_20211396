package com.example1.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example1.demo.model.domain.Article;

@Repository
public interface BlogRepository extends JpaRepository<Article, Long>{

    // 제목이나 내용에 검색어가 포함된 게시글을 찾아서 페이지 단위로 반환
    Page<Article> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
}