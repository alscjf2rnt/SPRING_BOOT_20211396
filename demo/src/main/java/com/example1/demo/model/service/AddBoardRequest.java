package com.example1.demo.model.service;

import lombok.*;
import java.time.LocalDate;
import com.example1.demo.model.domain.Board;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddBoardRequest {
    private String title;
    private String content;
    private String user;
    private LocalDate newDate; 
    private int count = 0; // 기본값 0
    private int likec = 0; // 기본값 0

    // setter 메서드 추가
    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    // toEntity 메서드
    public Board toEntity() { 
        return Board.builder()
                .title(title)
                .content(content)
                .user(user != null ? user : "GUEST") // user가 없으면 "GUEST"로 설정
                .newdate(newDate != null ? newDate : LocalDate.now()) // 작성일자가 없다면 오늘 날짜로 설정
                .count(count) // 기본값 0
                .likec(likec) // 기본값 0
                .build();
    }
}
