package com.example1.demo.model.domain;

import lombok.*; // 어노테이션 자동 생성
import jakarta.persistence.*; // 기존 javax 후속 버전

@Getter // setter는 없음 (무분별한 변경을 막기 위함)
@Entity // 아래 객체와 DB 테이블을 매핑. JPA가 관리
@Table(name = "article") // 테이블 이름을 지정. 없는 경우 클래스 이름으로 설정
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부 생성자 접근 방지
@AllArgsConstructor // 모든 필드를 받는 생성자 자동 생성
@Builder // 빌더 패턴 자동 생성
public class Article {
    @Id // 기본 키
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 1씩 증가
    @Column(name = "id", updatable = false) // 수정 불가능
    private Long id;

    @Column(name = "title", nullable = false) // null 불가
    private String title = "";

    @Column(name = "content", nullable = false)
    private String content = "";

    @Column(name = "user") // `user` 필드 추가 (필요시 nullable 설정)
    private String user;

    @Column(name = "newDate") // `newDate` 필드 추가
    private String newDate;

    @Column(name = "viewCount") // `viewCount` 필드 추가
    private int viewCount;

    @Column(name = "likes") // `likes` 필드 추가
    private Integer likes; // 수정: int -> Integer로 변경하여 null 값을 처리할 수 있도록

    // 게시글 수정 메서드
    public void update(String title, String content, String user, String newDate, int viewCount, Integer likes) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.newDate = newDate;
        this.viewCount = viewCount;
        if (likes != null) {
            this.likes = likes;
        } else {
            this.likes = 0; // likes가 null일 경우 0으로 설정
        }
    }
}
