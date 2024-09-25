package com.example1.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class DemoController {
    @GetMapping("/hello")
    public String hello(Model model) {  // 매소드 이름이 다름 '모델'이라는 '객체', '데이터'라는 '변수' 세팅을 할때는 직접세팅을 할수있고
    model.addAttribute("data","반갑습니다.");
    return "hello"; 
    }
    @GetMapping("/about_detailed")
    public String about() {         // 이페이지는 변수세팅없이 단순 링크다 하면 파라미터 비우시고 명시하시면 됩니다.
    return "about_detailed";
    }
}

