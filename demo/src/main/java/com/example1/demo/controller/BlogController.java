package com.example1.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import com.example1.demo.model.domain.Article;
import com.example1.demo.model.service.BlogService;

@Controller   //컨트롤러 이노테이션 명시
public class BlogController {

    @Autowired
    BlogService blogService; // DemoController 클래스 아래 객체 생성

    @GetMapping("/article_list")   //게시판 링크 지정
    public String article_list(Model model) {
    List<Article> list = blogService.findAll(); // 게시판 리스트
    model.addAttribute("articles", list); // 모델에 추가
    return "article_list"; // .HTML 연결
    }

}
