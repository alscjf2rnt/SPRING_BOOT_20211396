package com.example1.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example1.demo.model.domain.Member;
import org.springframework.validation.BindingResult; 
import javax.validation.Valid;  // @Valid 어노테이션 임포트
import com.example1.demo.model.service.AddMemberRequest;
import com.example1.demo.model.service.MemberService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller // 컨트롤러 이노테이션 명시
public class MemberController {
    
    @Autowired // 스프링에서 MemberService를 자동으로 주입받음
    private MemberService memberService;

    @GetMapping("/join_new") // 회원 가입 페이지 연결
    public String join_new() {
        return "join_new"; // .HTML 연결
    }

    @PostMapping("/api/members")
    public String addmembers(@Valid @ModelAttribute AddMemberRequest request, BindingResult result, Model model) {
    if (result.hasErrors()) {
        // 에러가 있으면 오류 메시지를 model에 담아 다시 화면으로 보냄
        model.addAttribute("errors", result.getAllErrors());
        return "join_new"; // 에러가 있으면 회원가입 화면으로 돌아감
    }
    memberService.saveMember(request); 
    return "join_end"; // 성공 시 회원가입 완료 화면으로 이동
}


    @GetMapping("/member_login") // 로그인 페이지 연결
    public String member_login() {
        return "login"; // .HTML 연결
    }

    @PostMapping("/api/login_check") // 로그인(아이디, 패스워드) 체크
    public String checkMembers(@ModelAttribute AddMemberRequest request, Model model) {
        try {
            Member member = memberService.loginCheck(request.getEmail(), request.getPassword()); // 패스워드 반환
            model.addAttribute("member", member); // 로그인 성공 시 회원 정보 전달
            return "redirect:/board_list"; // 로그인 성공 후 이동할 페이지
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 올바르지 않습니다."); // 실패 메시지 설정
            return "login"; // 로그인 실패 시 로그인 페이지로 리다이렉트
        }
    }
    @PostMapping("/add")
    public String addMember(@Valid @RequestBody AddMemberRequest addMemberRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 검증 실패 시 오류 메시지 반환
            return "입력값이 유효하지 않습니다: " + bindingResult.getAllErrors();
        }

        // 검증이 성공적으로 끝났다면 엔티티 변환
        Member member = addMemberRequest.toEntity();

        // 회원 정보를 저장하는 로직 추가 (예: memberRepository.save(member))
        return "회원가입이 성공적으로 완료되었습니다.";
    }
}
