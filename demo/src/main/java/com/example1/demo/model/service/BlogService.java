package com.example1.demo.model.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.example1.demo.model.domain.Article;
import com.example1.demo.model.domain.Board;
import com.example1.demo.model.repository.BlogRepository;
import com.example1.demo.model.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // 생성자 자동 생성(부분)
public class BlogService {
    @Autowired // 객체 주입 자동화, 생성자 1개면 생략 가능
    private final BlogRepository blogRepository; // 리포지토리 선언
    
    @Autowired
    private final BoardRepository boardRepository;

    public Page<Article> findAllArticles(Pageable pageable) {// 게시판 전체 목록 조회
        return blogRepository.findAll(pageable);
    }


    // 게시글 좋아요 수 증가
    public void incrementLikeCount(Long boardId) {
    Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    board.setLikec(board.getLikec() + 1);  // 좋아요 수 1 증가
    boardRepository.save(board);  // 업데이트된 게시글 저장
}


 // 게시글 상세 조회 메서드
 public Board getBoardById(Long boardId) { 
    return boardRepository.findById(boardId)
            .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
}
     // Board 저장 처리
     public Board saveBoard(Board board) {
        return boardRepository.save(board); // Board 객체를 저장
    }
 // 검색어로 게시글 검색
 public Page<Article> searchArticlesByKeyword(String keyword, PageRequest pageable) {
    return blogRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable); // 제목이나 내용에서 키워드를 포함하는 게시글 검색
}
 // 게시판 검색
 public Page<Board> searchBoardsByKeyword(String keyword, Pageable pageable) {
    return boardRepository.findByTitleContainingIgnoreCase(keyword, pageable); // 검색된 게시판 목록 조회
}
    public Article save(AddArticleRequest request) {
        // DTO가 없는 경우 이곳에 직접 구현 가능
        // public ResponseEntity<Article> addArticle(@RequestParam String title, @RequestParam String content) {
        // Article article = Article.builder()
        // .title(title)
        // .content(content)
        // .build();
        return blogRepository.save(request.toEntity());
    }

//  // Board 저장
// public Board saveBoard(AddArticleRequest request) {
//     Board board = new Board();
//     board.setTitle(request.getTitle()); // AddArticleRequest의 title을 Board의 title로 설정
//     board.setContent(request.getContent()); // content도 마찬가지
//     board.setUser(request.getUser()); // user 설정
//     board.setNewDate(LocalDate.parse(request.getNewDate()));
//     board.setCount(0);  // 조회수 기본값 0 (int로 설정)
//     board.setLikec(0);  // 좋아요 수 기본값 0 (int로 설정)
//     return boardRepository.save(board); // Board 객체를 저장
    
// }
// Board 저장
public Board saveBoard(AddArticleRequest request) {
    Board board = new Board();
    board.setTitle(request.getTitle()); // AddArticleRequest의 title을 Board의 title로 설정
    board.setContent(request.getContent()); // content도 마찬가지

      // 로그인한 사용자의 이메일을 가져와 '@' 앞부분만 추출
      String loggedInUserEmail = getLoggedInUserEmail();
      String userName = extractUserNameFromEmail(loggedInUserEmail);
      board.setUser(userName); // '@' 앞부분만 저장
  

    // newDate가 null일 경우 LocalDate.now()로 처리
    String newDateString = request.getNewDate();
    if (newDateString != null) {
        try {
            board.setNewDate(LocalDate.parse(newDateString)); // String을 LocalDate로 변환하여 set
        } catch (Exception e) {
            // 예외 발생 시 처리
            throw new IllegalArgumentException("잘못된 날짜 형식입니다. 올바른 형식을 사용해주세요.");
        }
    } else {
        // newDate가 null일 경우 현재 날짜를 기본값으로 설정
        board.setNewDate(LocalDate.now());
    }

    board.setCount(0);  // 조회수 기본값 0 (int로 설정)
    board.setLikec(0);  // 좋아요 수 기본값 0 (int로 설정)
    return boardRepository.save(board); // Board 객체를 저장
}

// 이메일에서 '@' 앞부분을 추출하는 메서드
private String extractUserNameFromEmail(String email) {
    if (email != null && email.contains("@")) {
        return email.split("@")[0]; // '@' 앞부분 반환
    }
    return "GUEST"; // 이메일이 null이거나 잘못된 형식이면 GUEST 반환
}
// 로그인한 사용자의 이메일을 반환하는 메서드 (예시: Spring Security 사용)
private String getLoggedInUserEmail() {
    // Spring Security에서 로그인한 사용자의 이메일을 가져오기
    return SecurityContextHolder.   getContext().getAuthentication().getName(); // 이메일 또는 사용자명
}



 // Board 저장
 public Board saveArticle(AddArticleRequest request) {
    Board board = new Board();
    board.setTitle(request.getTitle()); // AddArticleRequest의 title을 Board의 title로 설정
    board.setContent(request.getContent()); // content도 마찬가지
    board.setUser(request.getUser()); // user 설정
    board.setNewDate(LocalDate.parse(request.getNewDate()));
    board.setCount(0);  // 조회수 기본값 0 (int로 설정)
    board.setLikec(0);  // 좋아요 수 기본값 0 (int로 설정)
    return boardRepository.save(board); // Board 객체를 저장
    
}

public Page<Board> findAllBoards(Pageable pageable) {
    return boardRepository.findAll(pageable); // Page<Board> 반환
}

 // Article 특정 글 조회
 public Optional<Article> findArticleById(Long id) { // 메소드명 수정
    return blogRepository.findById(id);
}

public List<Board> findAllBoardsFromArticles(Pageable pageable) {
    Page<Article> articlesPage = blogRepository.findAll(pageable);
    return convertArticlesToBoards(articlesPage.getContent());
}

public List<Board> convertArticlesToBoards(List<Article> articles) {
    List<Board> boards = new ArrayList<>();
    for (Article article : articles) {
        Board board = new Board();
        board.setTitle(article.getTitle());
        board.setContent(article.getContent());
        board.setUser(article.getUser());
        board.setNewDate(LocalDate.parse(article.getNewDate())); // String을 LocalDate로 변환하여 set
        board.setCount(article.getViewCount());
        board.setLikec(article.getLikes());
        boards.add(board);
    }
    return boards;
}

// Board 특정 글 조회
public Optional<Board> findBoardById(Long id) {
    return boardRepository.findById(id);
}


    // // 게시글 수정
    // public void update(Long id, AddArticleRequest request) {
    //     Optional<Article> optionalArticle = blogRepository.findById(id); // 단일 글 조회
    //     optionalArticle.ifPresent(article -> { // 값이 있으면
    //         article.update(request.getTitle(), request.getContent()); // 값을 수정
    //         blogRepository.save(article); // Article 객체에 저장
    //     });
    // }
    // public void update(Long id, AddArticleRequest request) {
    //     Optional<Article> optionalArticle = blogRepository.findById(id); // Article 조회
    //     optionalArticle.ifPresent(article -> { // 값이 존재하면
    //         article.update( // Article 객체의 update 메소드 호출
    //             request.getTitle(), 
    //             request.getContent(), 
    //             request.getUser(), 
    //             request.getNewDate(), 
    //             request.getViewCount(), 
    //             request.getLikes()
    //         );
    //         blogRepository.save(article); // 수정된 Article 객체 저장
    //     });
    // }
    
    public void update(Long id, AddBoardRequest request) {
        Optional<Board> optionalBoard = boardRepository.findById(id); // Board 조회
        optionalBoard.ifPresent(board -> { // 값이 존재하면
            board.setTitle(request.getTitle()); // title 수정
            board.setContent(request.getContent()); // content 수정
            
            // 작성자가 null이면 기존 작성자 유지
            board.setUser(request.getUser() != null ? request.getUser() : board.getUser());
            
            // newDate가 null이면 기존 날짜 유지
            board.setNewDate(request.getNewDate() != null ? request.getNewDate() : board.getNewdate());
            
            board.setCount(request.getCount()); // 조회수 수정
            board.setLikec(request.getLikec()); // 좋아요 수 수정
    
            boardRepository.save(board); // 수정된 Board 객체 저장
        });
    }
    
    
    
//   // 게시글 수정
//   public void update(Long id, AddArticleRequest request) {
//     Optional<Article> optionalArticle = blogRepository.findById(id); // 단일 글 조회
//     optionalArticle.ifPresent(article -> { // 값이 있으면
//         article.update(request.getTitle(), request.getContent(), request.getUser(), request.getNewDate(), request.getViewCount(), request.getLikes());
//         blogRepository.save(article); // 수정된 Article 객체 저장
//     });
// }

    public void updateBoard(Board board) {
    // boardRepository는 JPA 리포지토리로 가정
    boardRepository.save(board); // 해당 게시글을 DB에 업데이트
}

     // 게시글 삭제
     public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    // // 게시글 조회 (findById 메소드 추가)
    // public Optional<Article> findById(Long id) {
    //     return blogRepository.findById(id); // Article 조회
    // }

    // 게시판 삭제
    @Transactional  // 트랜잭션 처리
    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<Board> searchByKeyword(Pageable pageable) {
        return boardRepository.findAll(pageable); // 메소드의 리턴 타입을 수정하고, 제대로 리턴 값을 반환
    }

    public Page<Board> searchByKeyword(String keyword, Pageable pageable) {
        return boardRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    } // LIKE 검색  제공(대소문자  무시)
        
}
