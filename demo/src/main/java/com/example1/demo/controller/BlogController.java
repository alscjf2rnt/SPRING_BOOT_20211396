package com.example1.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import com.example1.demo.model.domain.Article;
import com.example1.demo.model.domain.Board;
import com.example1.demo.model.service.AddArticleRequest;
import com.example1.demo.model.service.BlogService;


@Controller   // 컨트롤러 이노테이션 명시
public class BlogController {

    @Autowired
    BlogService blogService; // DemoController 클래스 아래 객체 생성

    // 게시판 목록 페이지
    @GetMapping("/article_list")   // 게시판 링크 지정
    public String article_list(Model model) {
        List<Article> list = blogService.findAllArticles(); // 게시판 리스트
        model.addAttribute("articles", list); // 모델에 추가
        return "article_list"; // .HTML 연결
    }   

    // @GetMapping("/board_view/{id}") // 게시판 링크 지정
    // public String board_view(Model model, @PathVariable Long id) {
    //     Optional<Board> boardOptional = blogService.findByBoardId(id); // 수정된 메소드 호출
    //     if (boardOptional.isPresent()) {
    //         model.addAttribute("board", boardOptional.get()); // Board 객체 추가
    //     } else {
    //         return "/error_page/article_error"; // 오류 처리 페이지 연결
    //     }
    //     return "board_view"; // .HTML 연결
    // }
    
 
 // 게시판 목록 페이지 (페이지네이션 및 검색 지원)
 @GetMapping("/board_list") // 새로운 게시판 링크 지정
 public String board_list(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "") String keyword) {
     // 페이지당 게시글 개수를 3으로 설정
     PageRequest pageable = PageRequest.of(page, 3);
     Page<Board> list; // Page를 반환

     // 검색어가 없으면 전체 게시글 조회, 있으면 검색한 게시글만 조회
     if (keyword.isEmpty()) {
         list = blogService.findAllBoards(pageable); // 기본 전체 출력(키워드 없이)
     } else {
         list = blogService.searchByKeyword(keyword, pageable); // 키워드로 검색
     }

        model.addAttribute("boards", list); // 모델에  추가
        model.addAttribute("totalPages", list.getTotalPages()); // 페이지  크기
        model.addAttribute("currentPage", page); // 페이지  번호
        model.addAttribute("keyword", keyword); // 키워드
        return "board_list"; // .HTML 연결
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
@GetMapping("/article_edit/{id}")
public String article_edit(Model model, @PathVariable Long id) {
    Optional<Article> articleOptional = blogService.findArticleById(id); // Article 조회
    if (articleOptional.isPresent()) {
        model.addAttribute("article", articleOptional.get()); // Article 객체 추가
        return "article_edit"; // .HTML 연결
    } else {
        return "/error_page/article_error"; // 오류 처리 페이지 연결
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


// 게시글 수정 화면 표시
@GetMapping("/board_edit/{id}") // 게시판 글 수정 페이지로 이동
public String board_edit(Model model, @PathVariable Long id) {
    Optional<Board> boardOptional = blogService.findBoardById(id); // 선택한 게시판 글
    if (boardOptional.isPresent()) {
        model.addAttribute("board", boardOptional.get()); // 존재하면 Board 객체를 모델에 추가
        return "board_edit"; // .HTML 연결
    } else {
        return "/error_page/article_error"; // 오류 처리 페이지로 연결
    }
}

// 게시글 수정 처리
@PutMapping("/api/board_edit/{id}") // 게시글 수정 API 처리
public String updateBoard(@PathVariable Long id, @ModelAttribute AddArticleRequest request) {
    blogService.update(id, request); // 수정된 게시글 저장
    return "redirect:/board_list"; // 수정 후 게시판 리스트 페이지로 리다이렉트
}

// 게시글 삭제 처리
@DeleteMapping("/api/board_delete/{id}") // 게시글 삭제 API 처리
public String deleteBoard(@PathVariable Long id) {
    blogService.delete(id); // 게시글 삭제
    return "redirect:/board_list"; // 삭제 후 게시판 리스트 페이지로 리다이렉트
}

@GetMapping("/board_view/{id}") // 게시판 링크 지정
public String board_view(Model model, @PathVariable Long id) { 
    Optional<Board> list = blogService.findBoardById(id); // 선택한 게시판 글
    if (list.isPresent()) {
        model.addAttribute("board", list.get()); // 존재할 경우 실제 Board 객체를 모델에 추가
    } else {
        return "/error_page/article_error"; // 오류 처리 페이지로 연결
    }
    return "board_view"; // .HTML 연결
}
@GetMapping("/board_write")
public String board_write() {
    return "board_write";
}
@PostMapping("/api/boards")
public String addboards(@ModelAttribute AddArticleRequest request) {
    blogService.saveBoard(request);  // 수정된 부분
    return "redirect:/board_list";
}



}