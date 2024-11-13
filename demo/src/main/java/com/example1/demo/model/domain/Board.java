package com.example1.demo.model.domain;

import lombok.*;
import jakarta.persistence.*;

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
    private String user = "";

    @Column(name = "newdate", nullable = false)
    private String newdate = "";

    @Column(name = "count", nullable = false)
    private int count = 0; // int로 수정

    @Column(name = "likec", nullable = false)
    private int likec = 0; // int로 수정

    // 기본 생성자 명시적으로 추가
    public Board() {}

    @Builder
    public Board(String title, String content, String user, String newdate, int count, int likec) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.newdate = newdate;
        this.count = count;
        this.likec = likec;
    }

    // update 메서드
    public void update(String title, String content, String user, String newdate, int count, int likec) {
        this.title = title;
        this.content = content;
        this.user = user;
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

    public void setUser(String user) {
        this.user = user;
    }

    public void setNewDate(String newDate) {
        this.newdate = newDate;
    }

    public void setLikec(int likec) {
        this.likec = likec;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
