package com.example1.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileController {

    @Value("${spring.servlet.multipart.location}") // properties 등록된 설정(경로) 주입
    private String uploadFolder;

    @PostMapping("/upload-email")
    public String uploadEmail(
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            RedirectAttributes redirectAttributes) {
        try {
            Path uploadPath = Paths.get(uploadFolder).toAbsolutePath();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath); // 업로드 폴더 생성
            }

            // 이메일을 안전하게 처리
            String sanitizedEmail = email.replaceAll("[^a-zA-Z0-9]", "_");
            Path filePath = uploadPath.resolve(sanitizedEmail + ".txt"); // .txt 파일 이름 설정
            System.out.println("File path: " + filePath); // 디버깅용 출력

            // 파일에 내용 쓰기
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
                writer.write("메일 제목: " + subject); // 제목 쓰기
                writer.newLine(); // 줄 바꿈
                writer.write("요청 메시지:");
                writer.newLine();
                writer.write(message);
            }

            redirectAttributes.addFlashAttribute("message", "메일 내용이 성공적으로 업로드되었습니다!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "업로드  중  오류가  발생했습니다.");
            return "/error_page/article_error"; // 오류  처리  페이지로  연결
            }
            return "redirect:/upload_end"; // .html 파일  연동
    }
     @GetMapping("/upload_end")
    public String uploadEndPage() {
        return "upload_end"; // upload_end.html로 연결
    }
}