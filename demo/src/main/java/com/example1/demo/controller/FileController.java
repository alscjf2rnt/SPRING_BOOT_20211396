package com.example1.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
@Controller
public class FileController {

    @Value("${spring.servlet.multipart.location}")
    private String uploadFolder;

    private static final String UPLOAD_DIR = "uploads/";
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024;
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif"};

    @PostMapping("/upload-email")
    public String uploadEmail(
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            RedirectAttributes redirectAttributes) {
        try {
            Path uploadPath = Paths.get(uploadFolder).toAbsolutePath();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String sanitizedEmail = email.replaceAll("[^a-zA-Z0-9]", "_");
            Path filePath = uploadPath.resolve(sanitizedEmail + ".txt");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
                writer.write("메일 제목: " + subject);
                writer.newLine();
                writer.write("요청 메시:");
                writer.newLine();
                writer.write(message);
            }
            redirectAttributes.addFlashAttribute("message", "메일 내용이 성공적으로 업로드되었습니다!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "업로드 중 오류가 발생했습니다.");
            return "error_page/article_error";  // 오류 처리 페이지로 연결
        }
        return "redirect:/upload_end"; // upload_end.html 페이지로 리다이렉트
    }

    @GetMapping("/upload_end")
    public String uploadEndPage() {
        return "upload_end";  // upload_end.html로 연결
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ROLE_USER')")  // ROLE_USER 권한이 있는 사용자만 업로드 가능
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "파일이 선택되지 않았습니다.");
            return "upload";
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            model.addAttribute("message", "파일 크기가 너무 큽니다. 최대 크기는 50MB입니다.");
            return "upload";
        }

        String originalFilename = file.getOriginalFilename();
        boolean isValidExtension = false;
        for (String ext : ALLOWED_EXTENSIONS) {
            if (originalFilename.toLowerCase().endsWith(ext)) {
                isValidExtension = true;
                break;
            }
        }

        if (!isValidExtension) {
            model.addAttribute("message", "허용되지 않는 파일 확장자입니다.");
            return "upload";
        }

        try {
            File dir = new File(uploadFolder);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Path path = Paths.get(uploadFolder + originalFilename);

            if (Files.exists(path)) {
                String newFilename = generateNewFilename(originalFilename);
                path = Paths.get(uploadFolder + newFilename);
            }

            file.transferTo(path.toFile());
            model.addAttribute("message", "파일 업로드 성공: " + path.toString());
            return "upload_end";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "파일 업로드 실패: " + e.getMessage());
            return "error";
        }
    }

    private String generateNewFilename(String originalFilename) {
        String fileNameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return fileNameWithoutExtension + "_" + timestamp + fileExtension;
    }

    @GetMapping("/upload")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showUploadForm() {
        return "upload";
    }
}
