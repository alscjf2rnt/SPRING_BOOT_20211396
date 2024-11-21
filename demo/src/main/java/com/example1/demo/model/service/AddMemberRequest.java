package com.example1.demo.model.service;

import lombok.*;
import javax.validation.constraints.*;
import com.example1.demo.model.domain.Member;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddMemberRequest {

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = "이름은 특수문자를 포함할 수 없습니다.")
    private String name;

    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,}$", message = "비밀번호는 8자 이상, 대소문자 및 숫자를 포함해야 합니다.")
    private String password;

    @NotBlank(message = "나이는 공백일 수 없습니다.")
    @Min(value = 19, message = "나이는 19세 이상이어야 합니다.")
    @Max(value = 90, message = "나이는 90세 이하이어야 합니다.")
    private Integer age;

    private String mobile; // 공백 허용

    private String address; // 공백 허용

    // Member 객체로 변환
    public Member toEntity() {
        return Member.builder()
            .name(this.name)
            .email(this.email)
            .password(this.password)
            .age(this.age)
            .mobile(this.mobile)
            .address(this.address)
            .build();
    }
}
