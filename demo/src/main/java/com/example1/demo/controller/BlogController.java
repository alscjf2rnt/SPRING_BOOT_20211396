package com.example1.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

import com.example1.demo.model.domain.Article;
import com.example1.demo.model.service.AddArticleRequest;
import com.example1.demo.model.service.BlogService;


@Controller   // 컨트롤러 이노테이션 명시
public class BlogController {

    @Autowired
    BlogService blogService; // DemoController 클래스 아래 객체 생성

    // 게시판 목록 페이지
    @GetMapping("/article_list")   // 게시판 링크 지정
    public String article_list(Model model) {
        List<Article> list = blogService.findAll(); // 게시판 리스트
        model.addAttribute("articles", list); // 모델에 추가
        return "article_list"; // .HTML 연결
    }
    
     // 게시글 작성 화면 표시
     @GetMapping("/article_write") // 게시글 작성 페이지로 이동하는 메소드 추가
     public String article_write(Model model) {
         return "article_write"; // 게시글 작성 페이지를 반환
     }
 
     // 게시글 작성 처리
     @PostMapping("/api/article_write") // 게시글 작성 요청을 처리하는 메소드 추가
     public String createArticle(@ModelAttribute AddArticleRequest request) {
         blogService.save(request); // 게시글 저장 처리
         return "redirect:/article_list"; // 목록 페이지로 리다이렉트
     }

// 게시글 수정 화면 표시
@GetMapping("/article_edit/{id}") // 게시판 링크 지정
public String article_edit(Model model, @PathVariable String id) {
    try {
        Long articleId = Long.parseLong(id); // 문자열을 Long으로 변환
        Optional<Article> articleOptional = blogService.findById(articleId); // 선택한 게시판 글

        if (articleOptional.isPresent()) {
            model.addAttribute("article", articleOptional.get()); // 존재하면 Article 객체를 모델에 추가
            return "article_edit"; // .HTML 연결
        } else {
            // 게시글을 찾을 수 없는 경우
            return "/error_page/article_error"; // 오류 처리 페이지로 연결
        }

    } catch (NumberFormatException e) {
        // id가 정수가 아닌 경우 처리
        return "/error_page/article_error2"; // 오류 처리 페이지로 연결
    }
}

    // 게시글 수정 처리
    @PutMapping("/api/article_edit/{id}")                               
    public String updateArticle(@PathVariable Long id, @ModelAttribute AddArticleRequest request) {
        blogService.update(id, request);
        return "redirect:/article_list"; // 글 수정 이후 .html 연결
    }

    // 게시글 삭제 처리
    @DeleteMapping("/api/article_delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/article_list";
    }
}
