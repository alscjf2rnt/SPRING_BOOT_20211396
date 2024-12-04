package com.example1.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example1.demo.model.domain.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
// List<Article> findAll();
Page<Board> findAll(Pageable pageable); // Pageable을 인자로 받아 Page<Board>를 반환
// 검색 기능 (키워드로 검색하는 메소드)

Page<Board> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}