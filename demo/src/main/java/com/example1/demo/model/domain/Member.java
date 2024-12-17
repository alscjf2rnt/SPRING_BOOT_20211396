package com.example1.demo.model.domain;

import lombok.*;
import jakarta.persistence.*;
import javax.validation.constraints.*;

@Entity
@Builder
@AllArgsConstructor
@Data
@Table(name = "Member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 1씩 증가
    @Column(name = "id", updatable = false) // 수정 x
    private Long id;

    @Column(name = "name", nullable = false) // null x
    @NotBlank(message = "Name is required") // 공백도 허용하지 않음
    private String name = "";

    @Column(name = "email", unique = true, nullable = false) // unique 중복 x
    @Email(message = "Invalid email format") // 이메일 형식 검사
    @NotBlank(message = "Email is required") // null 및 빈 문자열 허용하지 않음
    private String email = "";

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is required") // 공백을 허용하지 않음
    private String password = "";

    @Column(name = "age", nullable = false)
    @NotNull(message = "나이는 필수 입력입니다.")
    @Min(value = 19, message = "나이는 19세 이상이어야 합니다.")
    @Max(value = 90, message = "나이는 90세 이하이어야 합니다.")
    private Integer age;

    @Column(name = "mobile", nullable = false)
    @NotBlank(message = "Mobile is required") // 공백 허용하지 않음
    private String mobile = "";

    @Column(name = "address", nullable = false)
    @NotBlank(message = "Address is required") // 공백 허용하지 않음
    private String address = "";

    // JPA를 위한 기본 생성자
    protected Member() {
    }

    // 이메일에서 아이디 부분만 추출하는 메서드
    public String getUserName() {
        if (this.email != null && this.email.contains("@")) {
            return this.email.split("@")[0]; // 이메일에서 '@' 앞부분만 추출
        }
        return this.email; // '@'가 없다면 그대로 반환
    }
}
