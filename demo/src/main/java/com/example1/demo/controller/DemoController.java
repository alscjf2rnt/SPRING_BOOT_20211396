package com.example1.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example1.demo.model.domain.TestDB;
import com.example1.demo.model.service.TestService;// 최상단 서비스 클래스 연동 추가

@Controller   //컨트롤러 이노테이션 명시
public class DemoController {

    @Autowired
    TestService testService; // DemoController 클래스 아래 객체 생성

    @GetMapping("/testdb")
    public String getAllTestDBs(Model model) {
        TestDB test = testService.findByName("홍길동");
        model.addAttribute("data4", test);
        System.out.println("데이터 출력 디버그 : " + test);
        return "testdb";
    }


    @GetMapping("/hello")  //전송방식 GET
    public String hello(Model model) {  // model 설정 , 매소드 이름이 다름 '모델'이라는 '객체', '데이터'라는 '변수' 세팅을 할때는 직접세팅을 할수있고
    model.addAttribute("data","반갑습니다.");
    return "hello";  // hello.html 연결
    }
    @GetMapping("/about_detailed")
    public String about() {         // 이페이지는 변수세팅없이 단순 링크다 하면 파라미터 비우시고 명시하시면 됩니다.
    return "about_detailed";
    }

    @GetMapping("/test1")
    public String thymeleaf_test1(Model model) {                 // 모델이라는 객체에 담아서 6가지의 변수를 가지고 test1로 가겠다
        model.addAttribute("data1", "<h2> 방갑습니다 </h2>");     // 적용하고 띄우는지, 문자열을 전체 띄우는지 차이
        model.addAttribute("data2", "태그의 속성 값");
        model.addAttribute("link", 01);
        model.addAttribute("name", "홍길동");
        model.addAttribute("para1", "001");
        model.addAttribute("para2", 002);
        return "thymeleaf_test1";
    }

    // @GetMapping("/acticle_list")
    // public String article_list() {
    //     return "article_list";
    // }
}