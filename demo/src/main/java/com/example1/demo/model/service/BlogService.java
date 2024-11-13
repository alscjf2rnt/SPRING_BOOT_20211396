package com.example1.demo.model.service;

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


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // 생성자 자동 생성(부분)
public class BlogService {
    @Autowired // 객체 주입 자동화, 생성자 1개면 생략 가능
    private final BlogRepository blogRepository; // 리포지토리 선언
    
    @Autowired
    private final BoardRepository boardRepository;

    public List<Article> findAllArticles() { // 게시판 전체 목록 조회
        return blogRepository.findAll();   
    }

 // Board 전체 목록 조회
 public List<Board> findAllBoards() { // 메소드명 수정
    return boardRepository.findAll();
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

 // Board 저장
public Board saveBoard(AddArticleRequest request) {
    Board board = new Board();
    board.setTitle(request.getTitle()); // AddArticleRequest의 title을 Board의 title로 설정
    board.setContent(request.getContent()); // content도 마찬가지
    board.setUser(request.getUser()); // user 설정
    board.setNewDate(request.getNewDate()); // newDate 설정
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
    public void update(Long id, AddArticleRequest request) {
        Optional<Article> optionalArticle = blogRepository.findById(id); // Article 조회
        optionalArticle.ifPresent(article -> { // 값이 존재하면
            article.update( // Article 객체의 update 메소드 호출
                request.getTitle(), 
                request.getContent(), 
                request.getUser(), 
                request.getNewDate(), 
                request.getViewCount(), 
                request.getLikes()
            );
            blogRepository.save(article); // 수정된 Article 객체 저장
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

     // 게시글 삭제
     public void delete(Long id) {
        blogRepository.deleteById(id);
    }

    // // 게시글 조회 (findById 메소드 추가)
    // public Optional<Article> findById(Long id) {
    //     return blogRepository.findById(id); // Article 조회
    // }

    // 게시판 삭제
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
