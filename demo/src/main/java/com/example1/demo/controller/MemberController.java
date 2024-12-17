package com.example1.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example1.demo.model.domain.Member;
import com.example1.demo.model.dto.AddMemberRequest;
import com.example1.demo.model.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import java.io.File;  
import java.io.IOException;

import javax.validation.Valid;

@Controller
public class MemberController {

    @Autowired // MemberService 자동 주입
    private MemberService memberService;

    // 회원 가입 페이지
    @GetMapping("/join_new")
    public String showJoinPage(Model model) {
        model.addAttribute("addMemberRequest", new AddMemberRequest());
        return "join_new"; // 회원가입 HTML 반환
    }

// // 회원가입 처리 (POST)
// @PostMapping("/join")
// public String join(@Valid @ModelAttribute AddMemberRequest user, BindingResult bindingResult, Model model) {
//     if (bindingResult.hasErrors()) {
//         return "join_new";  // 폼에 오류가 있을 경우 다시 폼 페이지로 리다이렉트
//     }
//     memberService.saveMember(user); // 회원가입 처리
//     return "redirect:/join_end"; // 회원가입 성공 후 리다이렉트
// }

// 회원가입 완료 페이지
@GetMapping("/join_end")
public String showJoinEndPage() {
    return "join_end"; // join_end.html을 반환
}

@PostMapping("/api/members")
public String addMember(@Valid @ModelAttribute AddMemberRequest addMemberRequest, 
                        BindingResult bindingResult, 
                        Model model) {
    if (bindingResult.hasErrors()) {
        // 오류 메시지가 있으면 폼으로 되돌려보냄
        model.addAttribute("addMemberRequest", addMemberRequest);
        return "join_new"; // 오류가 있으면 다시 회원가입 페이지로
    }
    // 유효성 검사를 통과하면, 회원가입 진행
    Member member = addMemberRequest.toEntity();
    memberService.saveMember(member);
    return "redirect:/join_end"; // 회원가입 성공 후 리다이렉트
}


    // 로그인 페이지
    @GetMapping("/member_login")
    public String member_login() {
        return "login"; // 로그인 HTML 반환
    }

    // 로그인 처리 코드 예시
    @PostMapping("/login")
    public String login(String email, String password, HttpSession session) {
        Member member = memberService.loginCheck(email, password); 
        // 로그인 검증 후 세션에 저장
        session.setAttribute("email", email); // 로그인한 사용자의 이메일 세션에 저장
        session.setAttribute("userId", member.getId()); 
        session.setAttribute("userName", member.getName()); // 사용자의 이름을 세션에 저장

        // 로그인 후 게시판 목록으로 리다이렉트
        return "redirect:/board_list";
    }

    @GetMapping("/session_info")
public String showSessionInfo(HttpSession session, Model model) {
    String userId = (String) session.getAttribute("userId");
    String userEmail = (String) session.getAttribute("email");

    model.addAttribute("userId", userId);  // userId 추가
    model.addAttribute("email", userEmail);  // email 추가

    return "session_info";  // session_info.html 템플릿을 반환
}

    // 로그인 처리 (세션과 쿠키 저장)
    @PostMapping("/api/login_check")
    public String checkMembers(@Valid @ModelAttribute AddMemberRequest request,
                               Model model,
                               HttpServletRequest httpRequest,
                               HttpServletResponse response) {
        try {
            // 로그인 로직 (이메일과 비밀번호 검증)
            Member member = memberService.loginCheck(request.getEmail(), request.getPassword());

            // 세션 생성 (기존 세션이 없으면 새로 생성)
            HttpSession session = httpRequest.getSession(true);

            // 사용자별 고유 세션 정보 저장
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
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        String originalFileName = file.getOriginalFilename();
        String fileName = originalFileName;
        File uploadDir = new File("/path/to/upload/directory");
    
        try {
            // 동일한 파일이 있으면 이름 변경
            if (new File(uploadDir, fileName).exists()) {
                String[] nameParts = originalFileName.split("\\.");
                String newFileName = nameParts[0] + "_" + System.currentTimeMillis() + "." + nameParts[1];
                fileName = newFileName;
            }
    
            // 파일 업로드
            File targetFile = new File(uploadDir, fileName);
            file.transferTo(targetFile);
    
        } catch (IOException e) {
            // 파일 업로드 중 오류 발생 시 에러 메시지 모델에 추가
            model.addAttribute("errorMessage", "파일 업로드 중 오류가 발생했습니다.");
            return "upload_error"; // 업로드 에러 페이지로 이동
        }
    
        // 업로드 성공 시 성공 페이지로 이동
        return "upload_success"; 
    }
    
}
