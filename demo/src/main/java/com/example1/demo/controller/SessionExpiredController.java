package com.example1.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Controller 어노테이션 추가
public class SessionExpiredController {
    
    @GetMapping("/session-expired")
    public String sessionExpired() {
        return "session-expired"; // session-expired.html을 반환
    }
}
