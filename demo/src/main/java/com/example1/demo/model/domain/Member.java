package com.example1.demo.model.domain;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Entity
@Table(name = "Member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 1씩 증가
    @Column(name = "id", updatable = false) // 수정 x
    private Long id;
    @Column(name = "name", nullable = false) // null x
    private String name = "";
    @Column(name = "email", unique = true, nullable = false) // unique 중복 x
    private String email = "";
    @Column(name = "password", nullable = false)
    private String password = "";

    @Column(name = "age", nullable = false)
    private Integer age;
    @Column(name = "mobile", nullable = false)
    private String mobile = "";
    @Column(name = "address", nullable = false)
    private String address = "";

    @Builder // 생성자에 빌더 패턴 적용(불변성)
    public Member(String name, String email, String password, Integer age, String mobile, String address){
    this.name = name;
    this.email = email;
    this.password = password;
    this.age = age;
    this.mobile = mobile;
    this.address = address;
    }
    
     // JPA를 위한 기본 생성자
     protected Member() {
    }
}