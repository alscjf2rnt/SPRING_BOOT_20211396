package com.example1.demo.model.domain;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
public class AddArticleRequest {
    private String title;
    private String content;
    private String user; // 로그인한 사용자를 자동으로 설정
    private LocalDate newdate; // 현재 날짜를 자동으로 설정
    private int count = 0; // 기본 조회수 0
    private int likec = 0; // 기본 좋아요 0

    // Board 엔티티로 변환하는 메서드
    public Board toEntity() {
        return Board.builder()
                    .title(this.title)
                    .content(this.content)
                    .user(this.user)
                    .newdate(this.newdate)
                    .count(this.count)
                    .likec(this.likec)
                    .build();
    }

    // count setter 메서드 추가
    public void setCount(int count) {
        this.count = count;
    }

    // likec setter 메서드 추가
    public void setLikec(int likec) {
        this.likec = likec;
    }
}
