package com.example1.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .headers(headers -> headers
                .addHeaderWriter((request, response) -> {
                    response.setHeader("X-XSS-Protection", "1; mode=block"); // XSS-Protection 헤더 설정
                })
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/upload", "/api/**") // /upload 경로와 /api/** 경로에 대해서 CSRF 보호 비활성화
            )
            .sessionManagement(session -> session
                .invalidSessionUrl("/session-expired") // 세션 만료 시 /session-expired URL로 리디렉션
                .maximumSessions(2) // 사용자별 세션 최대 수
                .maxSessionsPreventsLogin(false) // 동시 세션 제한
            );

        return http.build(); // 필터 체인을 통해 보안 설정(HttpSecurity)을 반환
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter(); // POST 메서드 외 다른 HTTP 메서드 처리
    }

    public String getLoggedInUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername(); // 이메일을 반환
            }
        }
        return null; // 인증되지 않은 경우 null 반환
    }
}
