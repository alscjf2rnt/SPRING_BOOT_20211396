package com.example1.demo.controller;

import java.io.IOException; 
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IOException.class)
    public String handleFileUploadException(IOException ex, Model model) {
        model.addAttribute("error", "파일 업로드 중 오류가 발생했습니다.");
        return "error"; // error.html 뷰 반환
    }
}
    