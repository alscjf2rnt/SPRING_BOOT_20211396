package com.example1.demo.model.domain;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "Board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title = "";

    @Column(name = "content", nullable = false)
    private String content = "";

    @Column(name = "user", nullable = false)
    private String user = "";  // 이메일 앞부분을 저장하는 필드

    @Column(name = "newdate", nullable = false)
    private LocalDate newdate = LocalDate.now();  // 기본값으로 오늘 날짜 설정

    @Column(name = "count", nullable = true)  // count는 null 허용
    private Integer count = 0;  // Integer로 변경하여 null 허용

    @Column(name = "likec", nullable = true)  // likec는 null 허용
    private Integer likec = 0;  // Integer로 변경하여 null 허용

    @Column(name = "email", nullable = false)  // 이메일 컬럼 (전체 이메일)
    private String email; 

    // 기본 생성자 명시적으로 추가
    public Board() {}

    @Builder
    public Board(String title, String content, String user, String email, LocalDate newdate, Integer count, Integer likec) {
        this.title = title;
        this.content = content;
        this.user = user;  // user 필드에 사용자 이메일 앞부분 저장
        this.email = email;  // email 필드에 전체 이메일 저장
        this.newdate = newdate;
        this.count = count;
        this.likec = likec;
    }

    // 이메일에서 앞부분만 추출하는 메서드
    public String getUserName() {
        if (this.email != null && this.email.contains("@")) {
            return this.email.split("@")[0]; // 이메일에서 '@' 앞부분만 반환
        }
        return this.email; // '@'가 없다면 그대로 반환
    }

    // update 메서드
    public void update(String title, String content, String user, String email, LocalDate newdate, Integer count, Integer likec) {
        this.title = title;
        this.content = content;
        this.user = user;  // 사용자 이메일 앞부분 저장
        this.email = email;  // 전체 이메일 저장
        this.newdate = newdate;
        this.count = count;
        this.likec = likec;
    }

    // setter 메서드 추가
    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNewDate(LocalDate newDate) {
        this.newdate = newDate;
    }

    public void setLikec(Integer likec) {
        this.likec = likec;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
