
package com.example1.demo.model.service;

import lombok.*; // 어노테이션 자동 생성
import com.example1.demo.model.domain.Article;
@Builder
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Data // getter, setter, toString, equals 등 자동 생성
public class AddArticleRequest {
    private String title;
    private String content;
    private String user; // 새로 추가된 필드
    private String newDate; // 새로 추가된 필드
    private int viewCount; // 새로 추가된 필드
    private int likes; // 새로 추가된 필드

        // 기존 toEntity 메서드 주석 처리
    /*
    public Article toEntity() { // Article 객체 생성
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
//     */
//  public void update(String title, String content, String user, String newDate, int viewCount, int likes) {
//         this.title = title;
//         this.content = content;
//         this.user = user;
//         this.newDate = newDate;
//         this.viewCount = viewCount;
//         this.likes = likes;
//     }
    // toEntity 메서드
    public Article toEntity() { 
        return Article.builder()
                .title(title)
                .content(content)
                .user(user)
                .newDate(newDate)
                .viewCount(viewCount)
                .likes(likes)
                .build();
                
    }
}

