package com.example1.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example1.demo.model.service.AddBoardRequest; // AddBoardRequest 클래스 경로 추가
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.example1.demo.model.domain.Article;
import com.example1.demo.model.domain.Board;
import com.example1.demo.model.service.AddArticleRequest;
import com.example1.demo.model.service.BlogService;

import jakarta.servlet.http.HttpSession;



@Controller   // 컨트롤러 이노테이션 명시
public class BlogController {

    @Autowired
    private BlogService blogService; // DemoController 클래스 아래 객체 생성

    // // 게시판 목록 페이지
    // @GetMapping("/article_list")   // 게시판 링크 지정
    // public String article_list(Model model) {
    //     List<Article> list = blogService.findAllArticles(); // 게시판 리스트
    //     model.addAttribute("articles", list); // 모델에 추가
    //     return "article_list"; // .HTML 연결
    // }   
//     @GetMapping("/article_list")
//     public String article_list(Model model, @RequestParam(defaultValue = "0") int page) {
//     // 페이지 번호와 한 페이지에 표시할 게시글 개수를 설정
//     Pageable pageable = PageRequest.of(page, 10);
    
//     // 페이징된 게시글 목록 조회
//     Page<Article> list = blogService.findAllArticles(pageable);

//     // 모델에 필요한 데이터 추가
//     model.addAttribute("articles", list);
//     model.addAttribute("totalPages", list.getTotalPages());
//     model.addAttribute("currentPage", page);
    
//     return "article_list";  // .html로 이동
// }

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
    
 
// 게시판 목록 페이지 (로그인 사용자 이메일 전달)
@GetMapping("/board_list")
public String board_list(
    Model model,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "") String keyword,
    HttpSession session
) {
    // 세션에서 userId와 email 가져오기
    String userId = (String) session.getAttribute("userId");
    String email = (String) session.getAttribute("email");

    if (userId == null || email == null) {
        return "redirect:/member_login"; // 세션 정보가 없으면 로그인 페이지로 리다이렉트
    }

    System.out.println("세션 userId: " + userId); // 디버깅용 출력
    System.out.println("세션 email: " + email); // 디버깅용 출력

    // 페이지당 게시글 개수를 3으로 설정
    int pageSize = 5;

    // 페이지 요청
    PageRequest pageable = PageRequest.of(page, pageSize);
    Page<Board> list;

    // 검색어에 따른 게시글 조회
    if (keyword.isEmpty()) {
        list = blogService.findAllBoards(pageable); // 전체 게시글 조회
    } else {
        list = blogService.searchByKeyword(keyword, pageable); // 검색된 게시글 조회
    }

     // 시작 번호 계산
     int startNum = (page * pageSize) + 1;
     
    // 모델에 필요한 데이터 추가
    model.addAttribute("boards", list.getContent()); // 게시글 목록
    model.addAttribute("currentPage", page); // 현재 페이지
    model.addAttribute("totalPages", list.getTotalPages()); // 전체 페이지 수
    model.addAttribute("keyword", keyword); // 검색어 전달
    model.addAttribute("email", email); // 로그인 사용자 이메일 추가
    model.addAttribute("startNum", startNum); // 시작 번호 추가
    model.addAttribute("boardList", list);  // 모델에 게시글 리스트와 이메일 추가

    return "board_list"; // 게시판 목록 페이지로 이동
}



@GetMapping("/board_write")
    public String showBoardWritePage(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            email = "GUEST"; // 로그인하지 않은 경우 기본값
        }
        model.addAttribute("email", email); // 이메일을 모델에 추가
        return "board_write"; // board_write.html로 이동
    }

    @PostMapping("/board_write")
    public String createBoard(Board board, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            email = "GUEST";  // 로그인하지 않은 경우 기본값 사용
        }
        board.setUser(email); // 게시글 작성자에 이메일 설정

        blogService.saveBoard(board); // 게시글 저장
        return "redirect:/board_list"; // 게시글 목록으로 리다이렉트
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

    @DeleteMapping("/api/board_delete/{id}")
    public String deleteBoard(@PathVariable Long id) {
    blogService.delete(id);
    return "redirect:/board_list";
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
public String updateBoard(@PathVariable Long id, @ModelAttribute AddBoardRequest request) {
    blogService.update(id, request); // 수정된 게시글 저장
    return "redirect:/board_list"; // 수정 후 게시판 리스트 페이지로 리다이렉트
}

// // 게시글 삭제 처리
// @DeleteMapping("/api/board_delete/{id}") // 게시글 삭제 API 처리
// public String deleteBoard(@PathVariable Long id) {
//     blogService.delete(id); // 게시글 삭제
//     return "redirect:/board_list"; // 삭제 후 게시판 리스트 페이지로 리다이렉트
// }


// @DeleteMapping("/api/board_delete/{id}")
// public String deleteBoard(@PathVariable Long id) {
//     System.out.println("삭제 요청 ID: " + id);  // 로그로 삭제되는 ID 확인
//     blogService.delete(id);
//     return "redirect:/board_list"; // 게시판 리스트로 리다이렉트
// }


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
// @GetMapping("/board_write")
// public String board_write() {
//     return "board_write";
// }
// @PostMapping("/board_write")
// public String createArticle(@ModelAttribute AddArticleRequest request, HttpSession session) {
//     String user = (String) session.getAttribute("email");
//     if (user != null) {
//         request.setUser(user); // 로그인된 사용자 설정
//         blogService.save(request); // 게시글 저장
//         return "redirect:/board_list"; // 목록 페이지로 리다이렉트
//     }
//     return "redirect:/login"; // 로그인 안 되어 있으면 로그인 페이지로 리다이렉트
// }
// @PostMapping("/api/boards")
// public String addboards(@ModelAttribute AddArticleRequest request) {
//     blogService.saveBoard(request);  // 수정된 부분
//     return "redirect:/board_list";
// }

@PostMapping("/api/boards")
public String addboards(@ModelAttribute AddBoardRequest  request, HttpSession session) {
    // 로그인한 사용자의 이메일을 세션에서 가져옵니다.
    String email = (String) session.getAttribute("email");
    
    // 이메일이 null이 아니라면 이메일을 설정
    if (email != null) {
        request.setEmail(email); // AddArticleRequest에 이메일 설정
    } else {
        request.setEmail("GUEST_EMAIL"); // 이메일이 없으면 기본값 설정
    }

    // Board 객체 생성 (Board.builder() 패턴 사용)
    Board board = Board.builder()
        .title(request.getTitle()) // 제목
        .content(request.getContent()) // 내용
        .user(request.getUser()) // 작성자
        .email(request.getEmail()) // 이메일을 request에서 가져옴
        .newdate(LocalDate.now())  // 오늘 날짜
        .count(0)  // 초기 조회수
        .likec(0)  // 초기 좋아요 수
        .build();

    // 게시글 저장
    blogService.saveBoard(board);

    return "redirect:/board_list"; // 게시글 목록으로 리다이렉트
}





}
