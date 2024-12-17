package com.example1.demo.model.dto;

import lombok.*;
import javax.validation.constraints.*;
import com.example1.demo.model.domain.Member;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddMemberRequest {

    // 이름은 필수 입력, 특수문자 포함 안됨
    @NotEmpty(message = "이름은 필수 입력입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "이름은 특수문자를 포함할 수 없습니다.")
    private String name;

    // 이메일은 공백 불허, 형식 체크
    @NotEmpty(message = "이메일은 필수 입력입니다.")
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    private String email;

    // 비밀번호는 필수 입력, 8자 이상, 대소문자 및 숫자 포함
    @NotEmpty(message = "비밀번호는 필수 입력입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,}$", message = "비밀번호는 8자 이상, 대소문자 및 숫자를 포함해야 합니다.")
    private String password;

    // 나이는 필수 입력, 19세 이상 90세 이하
    @NotNull(message = "나이는 필수 입력입니다.")
    @Min(value = 19, message = "나이는 19세 이상이어야 합니다.")
    @Max(value = 90, message = "나이는 90세 이하이어야 합니다.")
    private Integer age;

    // 전화번호는 공백 허용
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호는 올바른 형식이어야 합니다.")
    private String mobile;

    // 주소는 공백 허용
    private String address;

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
