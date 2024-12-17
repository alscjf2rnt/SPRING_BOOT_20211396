package com.example1.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example1.demo.model.domain.Member;
import com.example1.demo.model.service.MemberService;
import com.example1.demo.model.dto.AddMemberRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.UUID;

@Controller
public class MemberController {

    @Autowired // MemberService 자동 주입
    private MemberService memberService;

    // 회원 가입 페이지
    @GetMapping("/join_new")
    public String join_new() {
        return "join_new"; // 회원가입 HTML 반환
    }

    // 회원 가입 처리
    @PostMapping("/api/members")
    public String addmembers(@Valid @ModelAttribute AddMemberRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "join_new"; // 에러 발생 시 회원가입 페이지로 돌아감
        }
        memberService.saveMember(request);
        return "join_end"; // 회원가입 성공 시 완료 페이지
    }

    // 로그인 페이지
    @GetMapping("/member_login")
    public String member_login() {
        return "login"; // 로그인 HTML 반환
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(String email, String password, HttpSession session, Model model) {
        try {
            Member member = memberService.loginCheck(email, password); 
            session.setAttribute("email", email); // 세션에 이메일 저장
            session.setAttribute("userId", member.getId()); // 세션에 사용자 ID 저장
            return "redirect:/board_list"; // 로그인 후 게시판 목록으로 리다이렉트
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "login"; // 실패 시 로그인 페이지로 돌아감
        }
    }

    // 로그인 처리 (세션과 쿠키 저장)
    @PostMapping("/api/login_check")
    public String checkMembers(@ModelAttribute AddMemberRequest request,
                               Model model,
                               HttpServletRequest httpRequest,
                               HttpServletResponse response) {
        try {
            // 로그인 로직 (이메일과 비밀번호 검증)
            Member member = memberService.loginCheck(request.getEmail(), request.getPassword());

            // 세션 생성 (기존 세션이 없으면 새로 생성)
            HttpSession session = httpRequest.getSession(true);

            // 사용자별 고유 세션 정보 저장
            // session.setAttribute("userId", UUID.randomUUID().toString()); // 고유 ID 저장
             session.setAttribute("userId", member.getUserName()); // 고유 사용자 ID를 사용
             session.setAttribute("email", request.getEmail()); // 사용자 이메일 저장

            // 사용자 이름 쿠키 생성 (선택 사항)
            Cookie userCookie = new Cookie("userEmail", request.getEmail());
            userCookie.setPath("/"); // 쿠키 경로 설정
            userCookie.setMaxAge(3600); // 쿠키 유효 시간: 1시간
            response.addCookie(userCookie);

            model.addAttribute("member", member); // 로그인 성공 시 사용자 정보 전달
            return "redirect:/board_list"; // 로그인 성공 후 이동할 페이지
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "login"; // 실패 시 로그인 페이지로 돌아감
        }
    }

    // 회원 정보 추가 (Optional)
    @PostMapping("/add")
    public String addMember(@Valid @RequestBody AddMemberRequest addMemberRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "입력값이 유효하지 않습니다: " + bindingResult.getAllErrors();
        }
        Member member = addMemberRequest.toEntity();
        return "회원가입이 성공적으로 완료되었습니다.";
    }

    // 로그아웃 처리 (세션과 쿠키 삭제)
    @GetMapping("/api/logout")
    public String member_logout(HttpServletRequest request, HttpServletResponse response) {
        // 현재 사용자의 세션 가져오기
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }

         // 모든 쿠키 삭제 (userEmail과 JSESSIONID)
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setPath("/"); // 쿠키 경로 설정
            response.addCookie(cookie);
        }
    }
    

    // JSESSIONID 쿠키 명시적으로 삭제
    Cookie jsessionCookie = new Cookie("JSESSIONID", null);
    jsessionCookie.setPath("/");
    jsessionCookie.setMaxAge(0);
    response.addCookie(jsessionCookie);

        return "redirect:/member_login"; // 로그아웃 후 로그인 페이지로 이동
    }

    
}
