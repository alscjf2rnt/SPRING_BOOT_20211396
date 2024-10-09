package com.example1.demo.model.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example1.demo.model.domain.Article;
import com.example1.demo.model.repository.BlogRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // 생성자 자동 생성(부분)
public class BlogService {
    @Autowired // 객체 주입 자동화, 생성자 1개면 생략 가능
    private final BlogRepository blogRepository; // 리포지토리 선언

        public List<Article> findAll() { // 게시판 전체 목록 조회
        return blogRepository.findAll();   
    }
}
